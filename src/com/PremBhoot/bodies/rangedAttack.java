package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class rangedAttack extends Structure{
    //boolean values to check if hit, total distance moved and if it has hit an entity (or should be cleared from tilemap)
    private boolean hitEntity, finished;
    private BufferedImage[] attack, hit;
    private double totalDistance;


    public rangedAttack(TileMap tm, boolean facingRight){
        super(tm);
        //run super (structure class) with tilemap parameter
        g=0.3; //set vertical and horizontal max velocities and accelerations
        terminalVelocity=3;
        dy=g;
        acceleration = 2;
        width = 32;
        height = 32;
        collisionWidth = 12;
        collisionheight = 20;
        if(facingRight){
            dx=acceleration; //check whether to go right or left
        } else{
            dx=-acceleration; //check whether to go right or left, parameter taken from player object
        }
        try{
            //get spritesheet of all of the ninja stars
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/AttackSprite.gif"));
            attack = new BufferedImage[3];
            hit = new BufferedImage[3];
            for(int i = 0; i<3; i++){
                attack[i] = spriteSheet.getSubimage(i*width, 0, width, height); //array of the ninja star moving
                hit[i] = spriteSheet.getSubimage(i*width, height, width, height); //array of the ninja star when it needs to be removed, i.e. has hit an object
            }
            animation = new Animation(); //animation creates for moving using attack array
            animation.setFrames(attack);
            animation.setDelay(50);


        } catch(Exception e){
            e.printStackTrace(); //try catch for importing files
        }
    }
    public void setHitEntity(){
        //function to change animation if hit entity and set velocity to 0
        hitEntity = true;
        animation.setFrames(hit);
        animation.setDelay(50);
        animation.update();
        dx=0;
    }
    public boolean isFinished(){return finished;} //getter
    public void update(){
        //use structure super method to check if it has collided with a tilemap to stop the rate of change in a certain direction (if tiles are touching)
        //if not set new position to velocity
        checkCollisionTileMap();
        setPos(xPosTemp, yPosTemp); //set new position to values calculated by check tilemap

        if(dx==0 && !hitEntity){
            setHitEntity(); //if it has stopped moving horizontally, set as hit (allow no vertical movement as it could move across the floor)

        } else if(totalDistance>1200){
            setHitEntity();
            finished = true; //if the ninja store has moved 1200 pixels, remove from tilemap
        }
        animation.update(); //update animations of ninja star
        if(hitEntity && animation.hasPlayed()){ //if it has hit a block and a new animation for hit has played, clear
            finished = true;
        }
        //repeated pythagoras calculation works out the total distance it has moved
        totalDistance += Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2));
    }
    public void draw(Graphics2D g){
        setMapPosition(); //needed for tilemap calculations to get positions to draw in
        if(facingRight){ //if facing right draw normally
            g.drawImage(animation.getImage(),
                    (int) (xPos + xPosMap - width/2),
                    (int) (yPos + yPosMap -height/2),
                    null);
        } else { //if facing left draw flipped
            g.drawImage(animation.getImage(),
                    (int) (xPos + xPosMap - width/2 + width), //add width at end so that if facing left it starts more from the left rather than moving across players body
                    (int) (yPos + yPosMap -height/2),
                    -width,
                    height,
                    null);
        }
    }

}
