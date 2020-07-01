package com.mygdx.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeScoreActivity extends AppCompatActivity {

    private ApiInterface apiInterface;

    private Toolbar toolbar;
    private TextInputLayout scoreTxt;
    private Button changeScroeBtn;
    private String userName;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_score);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        userId = getIntent().getStringExtra("user_id");
        userName = getIntent().getStringExtra("user_name");

        scoreTxt = findViewById(R.id.changescore_score);
        changeScroeBtn = findViewById(R.id.changeScore_change);


        toolbar = findViewById(R.id.changescore_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Change " + userName + "'s Score");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getHighScore(userName);

        changeScroeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               int scoreInt = Integer.parseInt(scoreTxt.getEditText().getText().toString());
                updateHighScore(userName,scoreInt);
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
                //Toast.makeText(HighScoresActivity.this,
                  //      t.getLocalizedMessage(),
                    //    Toast.LENGTH_SHORT).show();
                //System.out.println("FAILED 2");
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
                    scoreTxt.getEditText().setText(String.valueOf(score));
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
