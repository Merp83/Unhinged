package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Player extends Structure{

    private int lives;
    private int maxLives;
    private int throwingKnives;
    private int maxAmmo;
    private boolean dead;
    private boolean stun;
    private long stunTime;

    private boolean attack;
    private int ammoCost;
    private int attackDamage;

    //private ArrayList<attacks> attacks;

    private boolean melee;
    private int meleeDamage;
    private int meleeRange;

    private boolean dash;

    private ArrayList<BufferedImage[]> sprites;
    private final int[] amount = {2, 4, 1, 2, 4, 2, 2};


    private static final int STATIONARY = 0;
    private static final int RUNNING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int DASH = 4;
    private static final int ATTACK = 5;
    private static final int MELEE = 6;

    public Player(TileMap tm){
        super(tm);
        width = 32;
        height = 32;
        collisionheight=20;
        collisionWidth=20;

        acceleration = 0.3;
        maxVelocity = 1.6;
        deceleration = 0.4;
        g = 0.15;
        terminalVelocity = 4.0;
        jumpS = -4.5;
        stopJump = 0.3;

        facingRight = true;

        lives = maxLives = 3;
        ammoCost = 10;
        throwingKnives = maxAmmo = 100;

        //attacks = new ArrayList<attacks>()
        meleeDamage = 2;
        meleeRange = 32;

        try{
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/UnhingedNinjaSprite.gif"));

            sprites = new ArrayList<BufferedImage[]>();
            for (int i = 0; i<7; i++){
                BufferedImage[] b = new BufferedImage[amount[i]];
                for(int j=0; j<amount[i];j++){
                    b[j] = spritesheet.getSubimage(j*width, i*height, width, height);
                }
                sprites.add(b);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        animation = new Animation();
        action = STATIONARY;
        animation.setFrames(sprites.get(STATIONARY));
        animation.setDelay(600);


    }
    public int getLives(){ return lives;}
    public int getMaxLives(){ return maxLives;}
    public int getThrowingKnives(){ return throwingKnives;}
    public int getMaxAmmo(){ return maxAmmo;}

    public void setAttack(){
        attack=true;
    }
    public void setMelee(){
        melee=true;
    }
    public void setDash(boolean dash){
        this.dash = dash;
    }
    private void getNextPosition(){
        if(left){
            dx -= acceleration;
            if(dx<-maxVelocity){
                dx=-maxVelocity;
            }
        } else if(right){
            dx+= acceleration;
            if(dx>maxVelocity){
                dx=maxVelocity;
            }
        } else {
            if(dx>0){
                dx-=deceleration;
                if(dx<0){
                    dx=0;
                }
            } else if(dx<0){
                dx+=deceleration;
                if(dx>0){
                    dx=0;
                }
            }
        }

        //cannot move whilst attacking
        if(((action == MELEE) || (action == ATTACK)) && !(jump || fall)){
            dx = 0;
        }

        if(jump && !fall){
            dy = jumpS;
            fall = true;
        }
        if (fall){
            if(dy >0 && dash){
                dy+=g*0.1;
            } else{
                dy+=g;
            }
            if(dy>0) jump =false;
            if(dy<0 && !jump) dy+=stopJump;
            if(dy> terminalVelocity) dy = terminalVelocity;
        }
    }
    public void update(){
        getNextPosition();
        checkCollisionTileMap();
        setPos(xPosTemp, yPosTemp);

        if(melee){
            if(action != MELEE){
                action = MELEE;
                animation.setFrames(sprites.get(MELEE));
                animation.setDelay(150);
                width=32;
            }
        } else if(attack){
            if(action!=ATTACK){
                action = ATTACK;
                animation.setFrames(sprites.get(ATTACK));
                animation.setDelay(150);
                width = 32;
            }
        } else if(dy>0){
            if(dash){
                if(action != DASH){
                    action = DASH;
                    animation.setFrames(sprites.get(DASH));
                    animation.setDelay(100);
                    width = 32;
                }
            } else if(action!=FALLING){
                action  = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width=32;
            }
        } else if(dy<0){
            if(action!=JUMPING){
                action = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(-1);
                width = 32;
            }
        } else if (left || right){
            if(action!=RUNNING){
                action = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(40);
                width = 32;
            }
        } else{
            if(action!=STATIONARY){
                action = STATIONARY;
                animation.setFrames(sprites.get(STATIONARY));
                width=32;
            }
        }
        animation.update();

        //direction
        if(action!=MELEE && action!=ATTACK){
            if(right) facingRight = true;
            if(left) facingRight = false;
        }
    }
    public void draw(Graphics2D g){
        setMapPosition();

        if(stun){
            long timeElapsed = (System.nanoTime() - stunTime) / 1000000;
            if(timeElapsed / 100 % 2 ==0){
                return;
            }
        }
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
