package com.mygdx.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.mygdx.game.main.Assets;
import com.mygdx.game.main.MainGame;

public class BlockButton extends Button{
    private static String[] images = {"block", "B_downLeft", "B_downRight", "B_upLeft", "B_upRight", "blockEnemy"};
    private int ref;
    private int numberOfStrike; // in case the block type is enemy
    private Pair position;

    public BlockButton(int ref, int x, int y, int width, int height, Assets assets) {
        super(images[ref], -1, assets);
        this.ref = ref;
        btn.getImage().setFillParent(true);
        btn.setPosition(x,y);
        position = new Pair(x,y);
        btn.setWidth(width);
        btn.setHeight(height);
        btn.addListener(new InputListener(){
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                updateTexture(assets);
                Gdx.app.log("TAG", event.toString());
                return true;
            }
        });
        numberOfStrike = 0;
    }

    public int getRef() {
        return ref;
    }

    public Pair getPosition() {
        return position;
    }

    private void updateTexture(Assets assets){
        if(ref !=5) {
            ref++;
            if (ref == images.length - 1) {
                ref = 0;
            }
            super.setImage(images[ref], assets);
        }
    }

    public void counterStrike(Assets assets){
        numberOfStrike--;
        if(numberOfStrike == 0){
            ref = 0;
            super.setImage(images[0],assets);
        }
        MainGame.getInstance().addToScore();
    }

    public void setEnemy(int numberOfStrike, Assets assets){
        this.numberOfStrike += numberOfStrike;
        ref = 5;
        super.setImage(images[ref], assets);
    }

    public int getX(){
        return (int) btn.getX();
    }
    public int getY(){
        return (int) btn.getY();
    }
}
