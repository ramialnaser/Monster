/*This class to manage player name and score*/
package com.mygdx.game.game;

public class Player {
    String name;
    int score;

    public Player(String name, int score) {
        this.name = name;
        this.score = score;
    }

    @Override
    public String toString() {
        return String.format("%-32s   %16d", name ,score );
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void setPlayer(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }
}
