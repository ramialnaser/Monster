/*All block, balls have pair to manage position as per map*/
package com.mygdx.game.game;


import java.util.Objects;

import static com.mygdx.game.main.Constant.BLOCK_SIZE;

public class Pair {
    private int x;
    private int y;

    public Pair() {
    }

    public Pair(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    //Getters
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }


    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void set(Pair pair){
        this.x = pair.getX();
        this.y = pair.getY();
    }


    public void add(int x, int y){
        this.x += x;
        this.y += y;
    }

    public void add(Pair pair){
        this.x += pair.x;
        this.y += pair.y;
    }

    @Override
    public String toString() {
        return x + ", " + y;
    }

    public boolean inCenter(){
        return x%BLOCK_SIZE == 0 && y%BLOCK_SIZE==0;
    }

    public boolean inEdge(){
        return (x%(BLOCK_SIZE/2) == 0 && y%BLOCK_SIZE == 0)||
                (x%BLOCK_SIZE == 0 && y%(BLOCK_SIZE/2)==0);
    }

    public boolean inEdgeHor(){
        return  x%(BLOCK_SIZE/2) == 0 && y%BLOCK_SIZE == 0;
    }

    public boolean inEdgeVer(){
        return  x%(BLOCK_SIZE) == 0 && y%(BLOCK_SIZE/2) == 0;
    }


    @Override
    public boolean equals(Object obj) {
        Pair p = (Pair)obj;
        return x== p.getX() && y == p.getY();
    }

    public Pair rightCell(){
        return new Pair(x+1,y);
    }
    public Pair upCell(){
        return new Pair(x,y+1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }


}
