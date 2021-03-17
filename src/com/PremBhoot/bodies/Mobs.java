package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import java.awt.*;

public class Mobs extends Structure{
    protected int lives, maxLives, meleeDamage;
    protected boolean dead, stun;
    protected long stunTime;

    public Mobs(TileMap tm) {
        super(tm);
    }
    public void update(){

    }
    public void draw(Graphics2D g){}
    public int getMeleeDamage(){return meleeDamage;}
    public int getLives(){return lives;}
    public int getMaxLives(){return maxLives;}
    public boolean isDead(){return dead;}
    public void hit(int damage){
        //this under
        if(dead) return;
        lives -=damage;
        if(lives<0) lives=0;
        if(lives==0) dead=true;
        stun=true;
        stunTime=System.nanoTime();

    }

}
