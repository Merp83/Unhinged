package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Boss extends Mobs {
    private BufferedImage[] sprite; //initial image of boss
    private BufferedImage[] oneArmless; //image of boss after sub 300 health
    private BufferedImage[] twoArmless; //image of boss after sub 120 health

    private boolean crazy1, crazy2;
    public Boss(TileMap tm) {
        super(tm);
        //this 2 under - variables to determine if the boss should go "crazy" - faster velocity, accleration and jumps initially
        //initially I had an error where the higher velocity and updating could cause the boss to go out of bands and escape the tilemap
        //this variable was introduced to ensure after every update cycle it would not jump and would only occur once - when the health went below 300 and 120 respectively
        //it could also go into a cycle of constantly adding and changing animations which would cause lag
        crazy1 = crazy2 = false;
        right=true; //initialy direction set, set health and movement
        facingRight = true;
        lives=maxLives=500;
        acceleration = 0.05;
        maxVelocity = 0.25;
        g = 0.3;
        terminalVelocity = 2;
        width=64; //boss sprite bigger than all other, 64x64 where tilesize = 32x32
        height=64;
        collisionWidth = 48;
        collisionheight = 54;

        try{
            //load spritesheets for all 3 states of the boss
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
            //before I had an error where the mobs would move in the air at the init stage of the first level state, where there positions are set
            //rate of change should be zero in air(where dy<=terminal vel)
            dx -= acceleration;
            if (dx < -maxVelocity) {
                dx = -maxVelocity;
            }
        } else if (right &&!fall) {
            dx += acceleration;
            //right opposite direction
        }
        if (dx > maxVelocity) {
            dx = maxVelocity;
            //prevents going over max velocity
        } else {
            if (dx > 0) {
                dx -= deceleration;
                if (dx < 0) {
                    dx = 0;
                    //if not going left or right and moving, decelerate
                }
            } else if (dx < 0) {
                dx += deceleration;
                if (dx > 0) {
                    dx = 0;
                }
            }
            if (fall) {
                dy += g;
                //if falling increase dy by g upto terminal velocity
            }
            //this under
            if(dy>terminalVelocity) dy = terminalVelocity;
        }
    }

    public void update(){
        //System.out.println(lives); testing
        //update functions get new dy and dx values, check collision then checks if the boss can move to these positions (using the collision detection method
        //from the structure class. Then determining whether it has collided or not, it sets the new x and y positions to the x and y pos temp values
        //from the collision detection method
        getNextPosition();
        checkCollisionTileMap();
        setPos(xPosTemp, yPosTemp);
        if(right && dx ==0){
            facingRight =false;
            right=false;
            left=true;  //if the boss has collided with something on the right, turn left
        } else if((left && dx==0) ||xPos<2300){
            facingRight = true;
            left=false;
            right=true; //if the boss has collided with something on the left, turn right
        }
        //further lag prevention - dont go into if loops if already been in before - as resetting animations demanding.
        if(lives <300 && !(crazy1 || crazy2)) {
            acceleration=0.4;   //if lives are less than 300, go crazy1 - high velocity, jump initially - harder to kill
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

            animation = new Animation(); //set to new sprite of armless
            animation.setFrames(oneArmless);
            animation.setDelay(1);
            crazy1 = true;
        }
        //oops was in else if before...
        if(lives<120 && !(crazy2)){
            g=0.05;                 //if not crazy 2 yet, and lives less than 120 go crazy 2 - even higher velocity - harder to kill
            acceleration=0.8;
            maxVelocity=2.8;
            dy=-4;


            animation = new Animation();    //set new sprite for bos
            animation.setFrames(twoArmless);
            animation.setDelay(1);
            crazy2 = true;

        }

    }
    public void draw(Graphics2D g) {
        //if(notOnScreen()) return;
        setMapPosition();   //required for all entities - gets positions from tilemap object

        if(facingRight){    //if facing right, draw sprite normally, +xPosMap - width/2 as positions and map positions do not align due to screen not
                        //showing entire tilemap and thus sets correct position for what is shown on the screen. divide width by 2 go align halfway between
                        //physical point
            g.drawImage(animation.getImage(),
                    (int) (xPos + xPosMap - width/2 ),
                    (int) (yPos + yPosMap -height/2),
                    null);
        } else {
            g.drawImage(animation.getImage(),   //if facing left, reflect using -width, from stack overflow
                    //https://stackoverflow.com/questions/9558981/flip-image-with-graphics2d
                    (int) (xPos + xPosMap - width/2 + width),
                    (int) (yPos + yPosMap -height/2),
                    -width,
                    height,
                    null);
        }
    }
}
