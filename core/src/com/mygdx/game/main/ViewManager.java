package com.mygdx.game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



public class ViewManager {
    private OrthographicCamera camera;
    private Viewport viewport;


    public ViewManager() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false);
        viewport = new ExtendViewport(Constant.WORLD_WIDTH, Constant.WORLD_HEIGHT, camera);
    }

    public void apply(SpriteBatch batch){
        viewport.apply();
        batch.setProjectionMatrix(camera.projection);
    }

    public void apply(SpriteBatch batch, Stage stage){
        viewport.apply();
        batch.setProjectionMatrix(camera.combined);
        stage.setViewport(viewport);

    }


    public Viewport getViewport() {
        return viewport;
    }


}
