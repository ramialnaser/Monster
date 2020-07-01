package com.mygdx.game.main;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class Assets {
    private AssetManager asset;
    private TextureAtlas atlas;

    public Assets() {
        asset = new AssetManager();
        asset.load("Basic.atlas", TextureAtlas.class);
        asset.finishLoading();
        atlas = asset.get("Basic.atlas");
    }

    public TextureAtlas.AtlasRegion getRegion(String name, int index) {
        return atlas.findRegion(name,index);
    }

    public TextureAtlas.AtlasRegion ball(){
        return atlas.findRegion("ball",-1);
    }

}
