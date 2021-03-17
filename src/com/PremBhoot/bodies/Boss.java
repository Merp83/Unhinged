package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Boss extends Mobs {
    private BufferedImage[] sprite;
    private BufferedImage[] oneArmless;
    private BufferedImage[] twoArmless;

    private boolean crazy1, crazy2;
    public Boss(TileMap tm) {
        super(tm);
        //this 2 under
        crazy1 = crazy2 = false;
        right=true;
        facingRight = true;
        lives=maxLives=500;
        acceleration = 0.05;
        maxVelocity = 0.25;
        g = 0.3;
        terminalVelocity = 2;
        width=64;
        height=64;
        collisionWidth = 48;
        collisionheight = 54;

        try{
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/boss.gif"));
            sprite = new BufferedImage[1];
            sprite[0] = spritesheet;
            animation = new Animation();
            animation.setFrames(sprite);
            animation.setDelay(1);

            //now only has to input stream once - save resources.
            BufferedImage spritesheet2 = ImageIO.read(getClass().getResourceAsStream("/ArmlessBoss.gif"));
            oneArmless = new BufferedImage[1];
            oneArmless[0] = spritesheet2;

            BufferedImage spritesheet3 = ImageIO.read(getClass().getResourceAsStream("/TwoArmlessBoss.gif"));
            twoArmless = new BufferedImage[1];

            //mistake - went invisible did not declare into array the bufferedimages
            twoArmless[0] = spritesheet3;



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
        } else if((left && dx==0) ||xPos<2300){
            facingRight = true;
            left=false;
            right=true;
        }
        //further lag prevention - dont go into if loops if already been in before - as resetting animations demanding.
        if(lives <300 && !(crazy1 || crazy2)) {
            acceleration=0.4;
            maxVelocity=1.3;
            g=0.05;
            dy=-3;
            //lag moved this - make game faster - optimise - moved to init, create animation when needed.
            /*try {
                BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/ArmlessBoss.gif"));
                sprite = new BufferedImage[1];
                sprite[0] = spritesheet;
                animation = new Animation();
                animation.setFrames(sprite);
                animation.setDelay(1);


            } catch (Exception e) {
                e.printStackTrace();
            }*/

            animation = new Animation();
            animation.setFrames(oneArmless);
            animation.setDelay(1);
            crazy1 = true;
        }
        //oops was in else if before...
        if(lives<120 && !(crazy2)){
            g=0.05;
            acceleration=0.8;
            maxVelocity=2.8;
            dy=-4;


            animation = new Animation();
            animation.setFrames(twoArmless);
            animation.setDelay(1);
            crazy2 = true;

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
