package com.mygdx.game;

class UserDetailsSingleton {

    private String username;
    private int score;

    private static final UserDetailsSingleton ourInstance = new UserDetailsSingleton();

    static UserDetailsSingleton getInstance() {
        return ourInstance;
    }

    private UserDetailsSingleton() {
    }

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
}
