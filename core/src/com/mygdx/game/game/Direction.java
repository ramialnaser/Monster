/*This enum to manage move direction*/
package com.mygdx.game.game;


import static com.mygdx.game.main.Constant.MOVE;

public enum Direction {

    LEFT(-MOVE, 0), RIGHT(MOVE, 0), UP(0, MOVE), DOWN(0, -MOVE), STOP(0,0);

    Direction(int x, int y) {
        pair = new Pair(x,y);
    }

    public Pair getPair() {
        return pair;
    }

    private Pair pair;

    public void move(Pair pair){
        pair.add(this.pair);
    }

}

