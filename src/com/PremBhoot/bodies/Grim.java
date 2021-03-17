package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Grim extends Mobs {
        private BufferedImage[] sprite;
    public Grim(TileMap tm) {
        super(tm);
        //this 2 under
        right=true;
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

        try{
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
        //this under
        if(left && !fall) {
            dx -= acceleration;
            if (dx < -maxVelocity) {
                dx = -maxVelocity;
            }
            } else if (right &&!fall) {
                dx += acceleration;
            }
            if (dx > maxVelocity) {
                dx = maxVelocity;
            } else {
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
            }
        }

    public void update(){
        //System.out.println(lives);
        getNextPosition();
        checkCollisionTileMap();
        setPos(xPosTemp, yPosTemp);
        if(stun) {
            long elapsed = (System.nanoTime() - stunTime) / 1000000;
            if (elapsed > 500) {
                stun = false;
            }
        }
            if(right && dx ==0){
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

        if(facingRight){
            g.drawImage(animation.getImage(),
                    (int) (xPos + xPosMap - width/2),
                    (int) (yPos + yPosMap -height/2),
                    null);
        } else {
            g.drawImage(animation.getImage(),
                    (int) (xPos + xPosMap - width/2 + width),
                    (int) (yPos + yPosMap -height/2),
                    -width,
                    height,
                    null);
        }
    }
}
