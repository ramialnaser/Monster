package com.mygdx.game.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class ExtendedSound {
    Sound sound;
    int counter;

    public ExtendedSound(String soundName) {
        sound = Gdx.audio.newSound(Gdx.files.internal(soundName));
        counter = 0;
    }

    void play(){
        sound.play();
        counter++;
    }

    public int getCounter() {
        return counter;
    }
}
