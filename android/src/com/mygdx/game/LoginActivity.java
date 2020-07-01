package com.mygdx.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity {

    private ImageView loginBg;
    private ImageView logo;
    private LoginButton faceLog;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    //Database
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private Button signInBtn;
    private Button signUpBtn;


    private ApiInterface apiInterface;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        faceLog = findViewById(R.id.facelog);
        loginBg = findViewById(R.id.login_bg);
        logo = findViewById(R.id.login_logo);


        signInBtn = findViewById(R.id.login_signInBtn);
        signUpBtn = findViewById(R.id.login_signUp);
        mAuth = FirebaseAuth.getInstance();


        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = new Intent(LoginActivity.this, SignInActivity.class);
                startActivity(signInIntent);

            }
        });

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signUpIntent);

            }
        });

        faceLog.setReadPermissions(Arrays.asList("email", "public_profile"));

        callbackManager = CallbackManager.Factory.create();

        faceLog.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                getUserprofile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void getUserprofile(final AccessToken accessToken) {

        GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {

                    // get the user information
                    String first_name = object.getString("first_name");
                    String last_name = object.getString("last_name");


                    isInDatabase(first_name + " " + last_name);

                    //Firebase
                    handleFacebookAccessToken(accessToken, first_name, last_name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();

    }

    private void handleFacebookAccessToken(AccessToken token, final String first_name, final String last_name) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(LoginActivity.this.getApplicationContext(), AndroidLauncher.class);
                            startActivity(intent);
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String uid = currentUser.getUid();

                            database = FirebaseDatabase.getInstance();
                            databaseReference = database.getReference().child("Users").child(uid);
                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", first_name + " " + last_name);
                            userMap.put("status", "Hi there, I'm using HKR Monsters App.");
                            userMap.put("role", "user");
                            databaseReference.setValue(userMap);

                        } else {

                        }
                    }


                });
    }

    //check if the user exists in database and if it doesnt call necessary method to add
    private void isInDatabase(final String username) {
        Call<List<HighScore>> usernameCall = apiInterface.getUsername(username);
        usernameCall.enqueue(new Callback<List<HighScore>>() {

            @Override
            public void onResponse(@NonNull Call<List<HighScore>> call, @NonNull Response<List<HighScore>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    try {
                        response.body().get(0).getUsername();
                    } catch (IndexOutOfBoundsException ex) {
                        addInitialScore(username);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HighScore>> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this,
                        t.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }


        });
    }

    //used to add the initial score 0 to db...
    private void addInitialScore(String username) {

        Call<HighScore> highScoreCall = apiInterface.addInitial(username, 0);

        highScoreCall.enqueue(new Callback<HighScore>() {
            @Override
            public void onResponse(@NonNull Call<HighScore> call, @NonNull Response<HighScore> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean success = response.body().isSuccess();
                    if (success) {
//                            Toast.makeText(HighScoresActivity.this,
//                                    response.body().getMessage(),
//                                    Toast.LENGTH_SHORT).show();
                        //finish();
                        System.out.println("SUCCESS");
                    } else {
//                            Toast.makeText(HighScoresActivity.this,
//                                    response.body().getMessage(),
//                                    Toast.LENGTH_SHORT).show();
                        //finish();
                        System.out.println("FAILED");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<HighScore> call, @NonNull Throwable t) {
                Toast.makeText(LoginActivity.this,
                        t.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
                System.out.println("FAILED 2");
            }
        });
    }

}
