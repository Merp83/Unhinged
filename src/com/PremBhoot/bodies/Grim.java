package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Grim extends Mobs {
        private BufferedImage[] sprite;
    public Grim(TileMap tm) {
        super(tm);
        //this 2 under - before did not have 2 variables and thus the grim would not start moving - had to add so checkNextPosition
        //add acceleration to dx.
        right=true;//initialy direction set, set health and movement
        facingRight = true;
        lives=maxLives=35;
        acceleration = 0.05;
        maxVelocity = 0.3;
        g = 0.3;
        terminalVelocity = 2;
        width=32;
        height=32;
        collisionWidth = 16;
        collisionheight = 28;

        try{    //get sprite of grim
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/grim.gif"));
            sprite = new BufferedImage[1];
            sprite[0] = spritesheet;
            animation = new Animation();
            animation.setFrames(sprite);
            animation.setDelay(1);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private void getNextPosition(){
        //this under - before would move whilst falling, changed
        if(left && !fall) {
            //if not falling and going left, add (negative) acceleration to change in x
            dx -= acceleration;
            if (dx < -maxVelocity) {
                dx = -maxVelocity; //limiting values
            }
        } else if (right &&!fall) {
                dx += acceleration; //if going right and not falling, add acceleration to change in x
            }
            if (dx > maxVelocity) {
                dx = maxVelocity; //limiting values
            } else {    //if not pressing left or right and moving, decelerate to 0
                if (dx > 0) {
                    dx -= deceleration;
                    if (dx < 0) {
                        dx = 0;
                    }
                } else if (dx < 0) {
                    dx += deceleration;
                    if (dx > 0) {
                        dx = 0;
                    }
                }
                if (fall) {
                    dy += g;
                }
                //this under
                if(dy>terminalVelocity) dy = terminalVelocity;
                //did not have this condition causing grims to move extremely fast from set position - limiting case
            }
        }

    public void update(){
        //System.out.println(lives); test
        //initially get next position depending on whether entity is falling or pressing left or right (this adjusts dx and dy values)
        //then check if there any collisions, if there are - do not update the relevant dimensions position (e.g. if colliding into wall do not allow to move into wall
        //set position to values calculated form 2 methods
        getNextPosition();
        checkCollisionTileMap();
        setPos(xPosTemp, yPosTemp);

            if(right && dx ==0){ //causes grims to move back and forth between blocks they collidie with - changing direction, natural ai
                facingRight =false;
                right=false;
                left=true;
            } else if(left && dx==0){
                facingRight = true;
                left=false;
                right=true;
            }

    }
    public void draw(Graphics2D g) {
        //if(notOnScreen()) return;
        setMapPosition();

        if(facingRight){ //draw normal
            g.drawImage(animation.getImage(),
                    (int) (xPos + xPosMap - width/2),
                    (int) (yPos + yPosMap -height/2),
                    null);
        } else {
            g.drawImage(animation.getImage(), //draw reflected
                    (int) (xPos + xPosMap - width/2 + width),
                    (int) (yPos + yPosMap -height/2),
                    -width,
                    height,
                    null);
            //https://stackoverflow.com/questions/9558981/flip-image-with-graphics2d
        }
    }
}
