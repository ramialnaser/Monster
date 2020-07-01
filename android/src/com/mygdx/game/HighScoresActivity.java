package com.mygdx.game;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HighScoresActivity extends AppCompatActivity {

    private ApiInterface apiInterface;

    private TextView username0;
    private TextView highscore0;
    private TextView username1;
    private TextView highscore1;
    private TextView username2;
    private TextView highscore2;
    private TextView username3;
    private TextView highscore3;
    private TextView username4;
    private TextView highscore4;
    private TextView username5;
    private TextView highscore5;
    private TextView username6;
    private TextView highscore6;
    private TextView username7;
    private TextView highscore7;
    private TextView username8;
    private TextView highscore8;
    private TextView username9;
    private TextView highscore9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.highscores_layout);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        username0 = findViewById(R.id.username0);
        highscore0 = findViewById(R.id.highscore0);
        username1 = findViewById(R.id.username1);
        highscore1 = findViewById(R.id.highscore1);
        username2 = findViewById(R.id.username2);
        highscore2 = findViewById(R.id.highscore2);
        username3 = findViewById(R.id.username3);
        highscore3 = findViewById(R.id.highscore3);
        username4 = findViewById(R.id.username4);
        highscore4 = findViewById(R.id.highscore4);
        username5 = findViewById(R.id.username5);
        highscore5 = findViewById(R.id.highscore5);
        username6 = findViewById(R.id.username6);
        highscore6 = findViewById(R.id.highscore6);
        username7 = findViewById(R.id.username7);
        highscore7 = findViewById(R.id.highscore7);
        username8 = findViewById(R.id.username8);
        highscore8 = findViewById(R.id.highscore8);
        username9 = findViewById(R.id.username9);
        highscore9 = findViewById(R.id.highscore9);

        getHighScoreList();

//        isInDatabase("nuno@hkr.se");
//        isInDatabase("tomates@hkr.se");
//        isHighScore("tomates@hkr.se", 5000);
//        getHighScore("nils@hkr.se");

    }

    private void getHighScoreList() {
        Call<List<HighScore>> highScoreCall = apiInterface.getHighScores();

        highScoreCall.enqueue(new Callback<List<HighScore>>() {
            @Override
            public void onResponse(@NonNull Call<List<HighScore>> call, @NonNull Response<List<HighScore>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<HighScore> highscores = response.body();
                    username0.setText(highscores.get(0).getUsername());
                    highscore0.setText(String.valueOf(highscores.get(0).getScore()));
                    username1.setText(highscores.get(1).getUsername());
                    highscore1.setText(String.valueOf(highscores.get(1).getScore()));
                    username2.setText(highscores.get(2).getUsername());
                    highscore2.setText(String.valueOf(highscores.get(2).getScore()));
                    username3.setText(highscores.get(3).getUsername());
                    highscore3.setText(String.valueOf(highscores.get(3).getScore()));
                    username4.setText(highscores.get(4).getUsername());
                    highscore4.setText(String.valueOf(highscores.get(4).getScore()));
                    username5.setText(highscores.get(5).getUsername());
                    highscore5.setText(String.valueOf(highscores.get(5).getScore()));
                    username6.setText(highscores.get(6).getUsername());
                    highscore6.setText(String.valueOf(highscores.get(6).getScore()));
                    username7.setText(highscores.get(7).getUsername());
                    highscore7.setText(String.valueOf(highscores.get(7).getScore()));
                    username8.setText(highscores.get(8).getUsername());
                    highscore8.setText(String.valueOf(highscores.get(8).getScore()));
                    username9.setText(highscores.get(9).getUsername());
                    highscore9.setText(String.valueOf(highscores.get(9).getScore()));

                    System.out.println("DONE");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<HighScore>> call, @NonNull Throwable t) {
                Toast.makeText(HighScoresActivity.this,
                        t.getLocalizedMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    //the below methods need to be moved to the appropriate classes.....

    //check if the user exists in database and if it doesnt call necessary method to add
//    private void isInDatabase(final String username) {
//        Call<List<HighScore>> usernameCall = apiInterface.getUsername(username);
//        usernameCall.enqueue(new Callback<List<HighScore>>() {
//
//            @Override
//            public void onResponse(@NonNull Call<List<HighScore>> call, @NonNull Response<List<HighScore>> response) {
//
//                if (response.isSuccessful() && response.body() != null) {
//                    try {
//                        response.body().get(0).getUsername();
//                    } catch (IndexOutOfBoundsException ex) {
//                        addInitialScore(username);
//                    }
//
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<HighScore>> call, @NonNull Throwable t) {
//                Toast.makeText(HighScoresActivity.this,
//                        t.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//
//        });
//    }

    //used to add the initial score 0 to db...
//    private void addInitialScore(String username) {
//
//        Call<HighScore> highScoreCall = apiInterface.addInitial(username, 0);
//
//        highScoreCall.enqueue(new Callback<HighScore>() {
//            @Override
//            public void onResponse(@NonNull Call<HighScore> call, @NonNull Response<HighScore> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    boolean success = response.body().isSuccess();
//                    if (success) {
////                            Toast.makeText(HighScoresActivity.this,
////                                    response.body().getMessage(),
////                                    Toast.LENGTH_SHORT).show();
//                        //finish();
//                        System.out.println("SUCCESS");
//                    } else {
////                            Toast.makeText(HighScoresActivity.this,
////                                    response.body().getMessage(),
////                                    Toast.LENGTH_SHORT).show();
//                        //finish();
//                        System.out.println("FAILED");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<HighScore> call, @NonNull Throwable t) {
//                Toast.makeText(HighScoresActivity.this,
//                        t.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
//                System.out.println("FAILED 2");
//            }
//        });
//    }

    //check if score is highscore and if so call method to update highscore
//    private void isHighScore(final String username, final int score) {
//
//        Call<List<HighScore>> highScoreCall = apiInterface.getScore(username);
//
//        highScoreCall.enqueue(new Callback<List<HighScore>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<HighScore>> call, @NonNull Response<List<HighScore>> response) {
//
//                if (response.isSuccessful() && response.body() != null) {
//                    if (response.body().get(0).getScore() < score) {
//                        updateHighScore(username, score);
//
//                        Toast.makeText(HighScoresActivity.this,
//                                "update highscore",
//                                Toast.LENGTH_SHORT).show();
//                    } else {
//
//                        Toast.makeText(HighScoresActivity.this,
//                                "dont update highscore",
//                                Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<HighScore>> call, @NonNull Throwable t) {
//                Toast.makeText(HighScoresActivity.this,
//                        t.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        });
//    }

    //updates highscore
//    private void updateHighScore(String username, int score) {
//
//        Call<HighScore> highScoreCall = apiInterface.updateHighScore(username, score);
//
//        highScoreCall.enqueue(new Callback<HighScore>() {
//            @Override
//            public void onResponse(@NonNull Call<HighScore> call, @NonNull Response<HighScore> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    boolean success = response.body().isSuccess();
//                    if (success) {
////                            Toast.makeText(HighScoresActivity.this,
////                                    response.body().getMessage(),
////                                    Toast.LENGTH_SHORT).show();
//                        //finish();
//                        System.out.println("SUCCESS");
//                    } else {
////                            Toast.makeText(HighScoresActivity.this,
////                                    response.body().getMessage(),
////                                    Toast.LENGTH_SHORT).show();
//                        //finish();
//                        System.out.println("FAILED");
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<HighScore> call, @NonNull Throwable t) {
//                Toast.makeText(HighScoresActivity.this,
//                        t.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
//                System.out.println("FAILED 2");
//            }
//        });
//
//    }

//    private void getHighScore(final String username) {
//
//        Call<List<HighScore>> highScoreCall = apiInterface.getScore(username);
//
//        highScoreCall.enqueue(new Callback<List<HighScore>>() {
//            @Override
//            public void onResponse(@NonNull Call<List<HighScore>> call, @NonNull Response<List<HighScore>> response) {
//
//                if (response.isSuccessful() && response.body() != null) {
//                    int score = response.body().get(0).getScore();
//                    System.out.println(String.valueOf(score).toUpperCase());
//
//                    Toast.makeText(HighScoresActivity.this,
//                            "got highscore",
//                            Toast.LENGTH_SHORT).show();
//                } else {
//
//                    Toast.makeText(HighScoresActivity.this,
//                            "didn't get highscore",
//                            Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<List<HighScore>> call, @NonNull Throwable t) {
//                Toast.makeText(HighScoresActivity.this,
//                        t.getLocalizedMessage(),
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        });
//    }
}
