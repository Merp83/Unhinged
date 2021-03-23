package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import java.awt.*;

public class Mobs extends Structure{ //although this class extends structure, it will become a super class
    //which is extended from by the grim and boss class (and any other mob entities to add in the future)

    protected int lives, maxLives, meleeDamage;
    protected boolean dead;
    protected long stunTime;

    public Mobs(TileMap tm) {
        super(tm);
    }
    public void update(){

    }
    //getters and setters
    public void draw(Graphics2D g){}
    public int getMeleeDamage(){return meleeDamage;}
    public int getLives(){return lives;}
    public int getMaxLives(){return maxLives;}
    public boolean isDead(){return dead;}
    public void hit(int damage){
        //this under - added if lives<0 lives==0 as some attacks exceeded damage of 1, so it would immediately become negative
        //also added if(dead) return as it could run hit even if entities were dead, due to update cycles
        if(dead) return;
        lives -=damage;
        if(lives<0) lives=0;
        if(lives==0) dead=true;


    }

}
