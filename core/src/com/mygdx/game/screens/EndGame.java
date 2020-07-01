package com.mygdx.game.screens;



import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.main.MainGame;


import static com.mygdx.game.main.Constant.BLOCK_SIZE;


public class EndGame implements Screen {
    Basic screen;
    private Texture background;
    String string;
    private int score;


    public EndGame(int score, boolean isWin) {
        screen = new Basic();
        this.score = score;
        if(isWin){
            string = "Congratulations\n Your score: " + score;
            //TODO send the socre to the server

            Preferences prefs = Gdx.app.getPreferences("prefs");
            prefs.putString("score", String.valueOf(score));
            prefs.flush();

        }else{
            string = "You Lost";
        }

    }

    @Override
    public void show() {
        background = new Texture("background2.png");


    }

    @Override
    public void render(float delta) {
        screen.render2ndColor();

        screen.batch.begin();
        screen.batch.draw(background,0,0);
        screen.font.draw(screen.batch, string, 6*BLOCK_SIZE, 5*BLOCK_SIZE);
        screen.batch.end();

        screen.stage.act();
        screen.stage.draw();
        if(screen.backButton()){
            MainGame.getInstance().setScreen(new LevelMap());
            dispose();
        }

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

}
