package com.mygdx.game.game;

import com.badlogic.gdx.utils.TimeUtils;
//The ball death timer (to avoid the disappear of ball directly
public class FadeTimer {
    int deathIn;
    long deathTime;

    public FadeTimer() {
        deathTime = TimeUtils.millis();
        deathIn = 2000; //Millis
    }

    public boolean isDie(){
        return TimeUtils.millis() > deathTime + deathIn;
    }
}
