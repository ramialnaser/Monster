/*Manage different screen*/

package com.mygdx.game.main;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.mygdx.game.screens.LevelMap;

public class MainGame extends Game {
    private static MainGame ourInstance = new MainGame();
    private static Game game;
    private int score;
    public MainGame() {
        game = new Game() {
            @Override
            public void create() {
                game.setScreen(new LevelMap());
            }
        };
        score = 0;
    }

    public static MainGame getInstance() {
        return ourInstance;
    }

    public Game getGame() {
        return game;
    }

    public void setScreen(Screen screen){
        game.setScreen(screen);

        //game.create();
    }

    public void resetScore(){
        this.score = 0;
    }
    public void addToScore(){
            this.score++;
    }

    public int getScore() {
        return score;
    }
    public String getStringScore() {
        return "SCORE: " + score;
    }

    @Override
    public Screen getScreen() {
        return screen;
    }



    @Override
    public void create() {
        game.create();
    }

    @Override
    public void render() {
        game.render();
    }

    @Override
    public void dispose() {
        game.dispose();
    }
}
