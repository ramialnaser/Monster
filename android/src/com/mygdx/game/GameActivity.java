package com.mygdx.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.main.MainGame;

public class GameActivity extends AndroidApplication {
        @Override
        protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
            initialize(new MainGame(), config);
        }
}
