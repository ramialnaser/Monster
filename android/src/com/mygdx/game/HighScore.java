package com.mygdx.game;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HighScore {

    @Expose
    @SerializedName("username") private String username;
    @Expose
    @SerializedName("score") private int score;
    @Expose
    @SerializedName("success") private boolean success;
    @Expose
    @SerializedName("message") private String message;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


}
