package com.mygdx.game;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.Interpolation;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;

import okhttp3.internal.platform.Platform;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AndroidLauncher extends AppCompatActivity  {

    private Button playButton;
    private Button chatButton;
    private Button howToPlayButton;

    private Button signOutButton;
    private Button shareButton;
    private Button highscoresButton;
    private Button screenTimeButton;
    private LoginManager loginManager;

    private TextView timertext;
    private ScreenTimer screenTimer;
    private boolean state;
    private ApiInterface apiInterface;


    //Firebase
    private FirebaseAuth mAuth;

    private InternetConnection connection = new InternetConnection();
    UserDetailsSingleton user = UserDetailsSingleton.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);



        /*
        if (connection.isOnline(getApplicationContext())) {
            System.out.println("ONLINE");
        } else {
            System.out.println("OFFLINE");
        }


        SharedPreferences pref = getApplicationContext().getSharedPreferences("prefs", MODE_PRIVATE);
        String username = pref.getString("username", null);

        if (username != null) {
        */
        setContentView(R.layout.layout);

        //Firebase
        mAuth = FirebaseAuth.getInstance();


        playButton = findViewById(R.id.startGame);
        chatButton = findViewById(R.id.chat);
        howToPlayButton = findViewById(R.id.howToPlay);
        signOutButton = findViewById(R.id.signOutBtn);
        shareButton = findViewById(R.id.sharebtn);
        highscoresButton = findViewById(R.id.highscoresButton);
        screenTimeButton = findViewById(R.id.screenTimeButton);

        timertext = findViewById(R.id.mainmenu_timer);
        screenTimer = new ScreenTimer();
        state = true;


        playButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), GameActivity.class);
                startActivity(intent);

            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        howToPlayButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HowToPlayActivity.class);
                startActivity(intent);
            }
        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ShareFB.class);
                startActivity(intent);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                loginManager.getInstance().logOut();
                Intent intent = new Intent(AndroidLauncher.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        highscoresButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (connection.isOnline(getApplicationContext())) {
                    Intent intent = new Intent(v.getContext(), HighScoresActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(AndroidLauncher.this,
                            "No connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        screenTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AndroidLauncher.this, ScreenTimeActivity.class);
                startActivity(intent);

            }
        });

       /* }

        else {
            Intent intent = new Intent(this.getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
        */
    }

    @Override
    public void onStart() {
        super.onStart();


        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
            DatabaseReference userRef = rootRef.child("Users").child(currentUser.getUid());

            try {


                userRef.child("role").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue().equals("admin")) {
                            Intent mainIntent = new Intent(AndroidLauncher.this, AdminActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(mainIntent);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            } catch (Exception ex) {
            }
        }
        // to check if it's logged in or not
        if (currentUser == null) {
            Intent intent = new Intent(AndroidLauncher.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        try {
            getUserName();
            isHighScore(user.getUsername(), fetchScorefromLibGDX());
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void getUserName() {

        final DatabaseReference sqldb = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getUid()).child("name");
        sqldb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue().toString();
                //System.out.println("User name: " + userName);
                UserDetailsSingleton user = UserDetailsSingleton.getInstance();
                user.setUsername(userName);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private int fetchScorefromLibGDX() {

        String score = "";
        try {
            Preferences prefs = Gdx.app.getPreferences("prefs");
            score = prefs.getString("score", "No name stored");
            if (!score.equals("No name stored")) {
                return Integer.parseInt(score);
            }
        } catch (NullPointerException ex) {
            System.out.println(ex.getMessage());
        }
        return 0;
    }

    //check if score is highscore and if so call method to update highscore
    private void isHighScore(final String username, final int score) {

        Call<List<HighScore>> highScoreCall = apiInterface.getScore(username);

        highScoreCall.enqueue(new Callback<List<HighScore>>() {
            @Override
            public void onResponse(@NonNull Call<List<HighScore>> call, @NonNull Response<List<HighScore>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().get(0).getScore() < score) {
                        updateHighScore(username, score);

//                        Toast.makeText(AndroidLauncher.this,
//                                "update highscore",
//                                Toast.LENGTH_SHORT).show();
                    } else {

//                        Toast.makeText(AndroidLauncher.this,
//                                "dont update highscore",
//                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HighScore>> call, @NonNull Throwable t) {
//                Toast.makeText(AndroidLauncher.this,
//                        t.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    //updates highscore
    private void updateHighScore(String username, int score) {

        Call<HighScore> highScoreCall = apiInterface.updateHighScore(username, score);

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
//                Toast.makeText(AndroidLauncher.this,
//                        t.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
                System.out.println("FAILED 2");
            }
        });

    }

    private void startTimer() {
        try {

            if (screenTimer.getMillieseconds() > 100) {
                screenTimer.setMillieseconds(0);
                screenTimer.setSeconds(screenTimer.getSeconds() + 1);
            }
            if (screenTimer.getSeconds() > 60) {
                screenTimer.setMillieseconds(0);
                screenTimer.setSeconds(0);
                screenTimer.setMinutes(screenTimer.getMinutes() + 1);
            }
            if (screenTimer.getMinutes() > 60) {
                screenTimer.setMillieseconds(0);
                screenTimer.setSeconds(0);
                screenTimer.setMinutes(0);
                screenTimer.setHours(screenTimer.getHours() + 1);
            }
            System.out.printf("%d:%d:%d:%d\n", screenTimer.getHours(), screenTimer.getMinutes()
                    , screenTimer.getSeconds(), screenTimer.getMillieseconds());


            timertext.setText(String.valueOf(screenTimer.getHours()
                    +" : "+screenTimer.getMinutes()+" : "+screenTimer.getSeconds()+" : "+screenTimer.getMillieseconds()));

            screenTimer.setMillieseconds(screenTimer.getMillieseconds() + 1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }




}
