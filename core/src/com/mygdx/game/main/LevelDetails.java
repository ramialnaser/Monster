package com.mygdx.game.main;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.StringBuilder;
import com.mygdx.game.game.Ball;
import com.mygdx.game.game.BlockButton;
import com.mygdx.game.game.EnemyGenerator;
import com.mygdx.game.game.ExtendedSound;
import com.mygdx.game.screens.Basic;
import com.mygdx.game.screens.EndGame;

import static com.mygdx.game.main.Constant.BLOCK_SIZE;

public class LevelDetails {
    private ExtendedSound metal;
    private ExtendedSound hitMonster;
    private ExtendedSound lostBall;

    private int level;
    private int ballsNo;
    private TextureRegion ball;

    private StringBuilder stringBuilder;
    private EnemyGenerator enemyGenerator;
    private Array<Ball> balls;

    private int timer;
    private boolean isRunning;

    public LevelDetails(int level, int numberOfEnemy, int ballsNo, int secondsBetween, Basic screen) {
        metal =  new ExtendedSound("ping.wav");
        hitMonster = new ExtendedSound("bongo.wav");
        lostBall = new ExtendedSound("loose.wav");

        timer = 3; // 3seconds
        this.level = level;
        this.ballsNo = ballsNo;
        ball = screen.getAssets().ball();

        balls = new Array<>();
        enemyGenerator = new EnemyGenerator(secondsBetween, numberOfEnemy);
        isRunning = false;
        stringBuilder = new StringBuilder();

    }

    public void update(Array<BlockButton> blockButtons, Assets assets, float timer){
        if(!isRunning){
            if(timer>1f){
                this.timer--;
                if(this.timer == 0){
                    isRunning = true;
                }
            }
        }else{
            if(timer>1f){
                this.timer++;
            }
        }
        //updateBall
        for (Ball b: balls) {
            b.ballMoving(blockButtons, metal, hitMonster, lostBall, assets);
        }
        for (int i = 0; i < balls.size ; i++) {
            if(balls.get(i).isKillBall()) balls.removeIndex(i);
        }
        //Update enemy
        enemyGenerator.update(blockButtons, assets, this.timer);

        if(ballsNo == 0 && balls.size == 0){
            MainGame.getInstance().setScreen(new EndGame(this.level*1000, false));
        }else if(enemyGenerator.getEnemyFixed()- hitMonster.getCounter() == 0) {
            MainGame.getInstance().setScreen(new EndGame(this.level*1000, true));
        }
    }

    public void generateBall(){
        if(isRunning && ballsNo >0){
            ballsNo--;
            balls.add(new Ball());
        }
    }

    public void draw(SpriteBatch batch, BitmapFont font){
        for (Ball b: balls) {
            b.draw(batch, ball);
        }
        stringBuilder = new StringBuilder();
        stringBuilder.append("Level " + level + "\n");
        if(!isRunning){
            stringBuilder.append("Start in: " + timer + "\n");
        }else{
            stringBuilder.append("Timer   : " + timer + "\n");
        }
        stringBuilder.append("Ball: " + ballsNo + "\n");
        stringBuilder.append("Enemy: " + (enemyGenerator.getEnemyFixed()-hitMonster.getCounter())+ "\n");
        font.draw(batch, stringBuilder, 40 , BLOCK_SIZE *6 -BLOCK_SIZE/2);
        if(ballsNo>0){
            batch.draw(ball, 13*BLOCK_SIZE, BLOCK_SIZE);
        }
    }



}
