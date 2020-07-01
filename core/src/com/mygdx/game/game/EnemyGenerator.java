package com.mygdx.game.game;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.main.Assets;

public class EnemyGenerator {
    int enemyFixed;
    private int secondsBetween; // the time between every two generator
    private int numberOfEnemy;
    private int nextEnemy;

    public EnemyGenerator(int secondsBetween, int numberOfEnemy) {
        this.secondsBetween = secondsBetween;
        this.numberOfEnemy = numberOfEnemy;
        enemyFixed = (numberOfEnemy);
        nextEnemy = secondsBetween;
    }

    public void update(Array<BlockButton> blockButtons, Assets assets, int time) {
        if (numberOfEnemy> 0 && time== nextEnemy) {
            int random;
            do random = MathUtils.random(1,48-1);
            while (random == 5 || random == 42);
            blockButtons.get(random).setEnemy(1, assets);
            numberOfEnemy--;
            nextEnemy += secondsBetween;
        }

    }

    public int getEnemyFixed() {
        return enemyFixed;
    }
}



