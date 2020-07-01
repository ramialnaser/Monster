package com.mygdx.game;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private ApiInterface apiInterface;
    private TextView name, status, scoreTxt;
    private ImageView profileImageView;

    private DatabaseReference usersDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        final String userId = getIntent().getStringExtra("user_id");

        profileImageView = findViewById(R.id.prof_image);
        name = findViewById(R.id.prof_userName);
        status = findViewById(R.id.prof_status);
        scoreTxt = findViewById(R.id.prof_scoreTxt);

        usersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);


        usersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String display_name = dataSnapshot.child("name").getValue().toString();
                String display_status = dataSnapshot.child("status").getValue().toString();
                name.setText(display_name);
                status.setText(display_status);

                getHighScore(display_name);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getHighScore(final String username) {


        Call<List<HighScore>> highScoreCall = apiInterface.getScore(username);

        highScoreCall.enqueue(new Callback<List<HighScore>>() {
            @Override
            public void onResponse(@NonNull Call<List<HighScore>> call, @NonNull Response<List<HighScore>> response) {

                if (response.isSuccessful() && response.body() != null) {
                    int score = response.body().get(0).getScore();
                    scoreTxt.setText(String.valueOf(score));
                    System.out.println(String.valueOf(score).toUpperCase());


                    // Toast.makeText(ProfileSettingsActivity.this,
                    //       "got highscore",
                    //     Toast.LENGTH_SHORT).show();
                } else {

                    //   Toast.makeText(ProfileSettingsActivity.this,
                    //         "didn't get highscore",
                    //       Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HighScore>> call, @NonNull Throwable t) {
                //  Toast.makeText(ProfileSettingsActivity.this,
                //        t.getLocalizedMessage(),
                //      Toast.LENGTH_SHORT).show();
            }

        });
    }
}
