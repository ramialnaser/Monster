package com.mygdx.game;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("insert.php")
        //adds a new user to the db
    Call<HighScore> addInitial(
            @Field("username") String username,
            @Field("score") int score
    );

    @FormUrlEncoded
    @POST("update.php")
        //update high score
    Call<HighScore> updateHighScore(
            @Field("username") String username,
            @Field("score") int score
    );

    @GET("highscores.php")
        //get the top 10 high scores in desc order
    Call<List<HighScore>> getHighScores();

    @FormUrlEncoded
    @POST("username.php")
        //returns the username if it exists (used to check if a new entry needs to be created)
    Call<List<HighScore>> getUsername(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("score.php")
        //returns the score (used to check if user's high score needs to be updated in db
    Call<List<HighScore>> getScore(
            @Field("username") String username
    );

    @FormUrlEncoded
    @POST("getHighScore.php")
        //returns the score (used to check if user's high score needs to be updated in db
    Call<List<HighScore>> getHighScore(
            @Field("username") String username
    );
}
