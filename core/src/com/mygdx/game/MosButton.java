package com.mygdx.game;

public class MosButton {
    float x, y;
    float width, height;

    public MosButton(float x, float y, float size){
        width = height = size;
        this.x = x;
        this.y = y;
    }

    boolean hit(float tx, float ty){
        if(x < tx && tx < x+width && y < ty && ty < y+height){
            return true;
        }
        return false;
    }
}
