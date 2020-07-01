package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.mygdx.game.main.Assets;
import com.mygdx.game.main.ViewManager;

public class Basic {
    SpriteBatch batch;
    Stage stage;
    ViewManager viewManager;
    Assets assets;
    BitmapFont font;

    public Basic() {
        batch = new SpriteBatch();
        viewManager = new ViewManager();
        stage = new Stage(viewManager.getViewport());

        assets = new Assets();
        font = new BitmapFont(Gdx.files.internal("baseFont.fnt"),false);

        font.setColor(Color.GREEN);
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(stage);
    }

    public void render(){

        Gdx.gl.glClearColor(0.90f, 0.90f, 0.90f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewManager.apply(batch, stage);

    }

    public void render2ndColor(){
        Gdx.gl.glClearColor(192f/255, 114f/255, 114f/255, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewManager.apply(batch, stage);
    }

    public Assets getAssets() {
        return assets;
    }

    public void dispose(){
        batch.dispose();
        stage.dispose();
    }

    public boolean backButton(){
        return Gdx.input.isKeyPressed(Input.Keys.BACK);
    }
}
