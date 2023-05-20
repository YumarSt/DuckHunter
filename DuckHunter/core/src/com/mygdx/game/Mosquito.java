package com.mygdx.game;

import static com.mygdx.game.MyGdxGame.SCR_HEIGHT;
import static com.mygdx.game.MyGdxGame.SCR_WIDTH;

import com.badlogic.gdx.math.MathUtils;

public class Mosquito {
    float x, y;
    float vx, vy;
    float width, height;
    int faza, nFaz = 10;
    boolean isAlive = true;

    public Mosquito(){
        width = height = MathUtils.random(100, 250);
        x = SCR_WIDTH /2-width/2;
        y = SCR_HEIGHT /2-height/2;
        vx = MathUtils.random(-5f, 5f);
        vy = MathUtils.random(-5f, 5f);
        faza = MathUtils.random(0, nFaz-1);
    }

    void fly(){
        x += vx;
        y += vy;
        if(isAlive) {
            outBounds1();
            if (++faza == nFaz) faza = 0;
        }
    }

    void outBounds1(){
        if(x<0 || x> SCR_WIDTH -width) vx = -vx;
        if(y<0 || y> SCR_HEIGHT -height) vy=-vy;
    }

    void outBounds2(){
        if(x<0-width) x = SCR_WIDTH;
        if(x> SCR_WIDTH) x = 0-width;
        if(y<0-height) y = SCR_HEIGHT;
        if(y> SCR_HEIGHT) y = 0-height;
    }

    boolean isFlip(){
        if(vx>0) {
            return true;
        }
        return false;
    }

    boolean hit(float tx, float ty){
        if(x < tx && tx < x+width && y < ty && ty < y+height){
            isAlive = false;
            faza = 10;
            vx = 0;
            vy = -8;
            return true;
        }
        return false;
    }
}