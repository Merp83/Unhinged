package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;
import com.PremBhoot.State.Music;

import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Player extends Structure {

    private long immunityBegin; //timings for immunity if hit by another entity - cannot be attacked in this time
    private boolean immunity;

    private double ninjaFCost; //floating costs, ammo and max (less gravity)
    private double ninjaFRegen;
    private double ninjaFMax;
    private double ninjaFAmmo;

    private double dashRegen; //dash costs, ammo, max and differing movement whilst dashing (velocity)
    private double dashCost;
    private double dashAmmo;
    private double maxDash;
    private double dashMaxVelocity;
    private double dashAcceleration;

    private int lives; //lives and maxlives
    private int maxLives;

    private double throwingKnives; //number of attacks (ammo), max ammo, damage to entities and cost to use
    private double maxAmmo;
    private double ammoCost;
    private int attackDamage;

    private boolean dead;


    private boolean attack; //if attacking


    public ArrayList<rangedAttack> attacks; //arraylist of ninja stars

    private boolean melee; //if doing melee attack, set to true
    private int meleeDamage;
    private int meleeRange;
    private Font titleFont; //not used
    private Color colour;

    private boolean dash; //if using dash or ninja f
    private boolean ninjaF;

    private ArrayList<BufferedImage[]> sprites; //buffered image array of sprites doing different actions
    private final int[] amount = {2, 4, 1, 2, 4, 2, 2, 4};

    //values to set when doing different actions (to change animation)
    private static final int STATIONARY = 0;
    private static final int RUNNING = 1;
    private static final int JUMPING = 2;
    private static final int FALLING = 3;
    private static final int DASH = 4;
    private static final int ATTACK = 5;
    private static final int MELEE = 6;
    private static final int NINJAF = 7;

    private Music jumpMusic;
    private float SFXvolume;

    public Player(TileMap tm) {
        super(tm);
        //call super with tilemap parameter
        //initialise all physical properties of player, dimensions, hitboxes, ammo, lives, movement speeds and accelerations
        width = 32;
        height = 32;
        collisionheight = 26;
        collisionWidth = 24;

        dashRegen = 0.09;
        dashAmmo = maxDash = 150;
        dashCost = 3;
        dashMaxVelocity = 3.5;
        dashAcceleration = 1.5;

        ninjaFRegen = 0.25;
        ninjaFAmmo = ninjaFMax = 750;
        ninjaFCost = 2;

        acceleration = 0.04;
        maxVelocity = 1.3;
        deceleration = 0.5;
        g = 0.045;
        terminalVelocity = 1.6;
        jumpS = -3;
        stopJump = 0.3;

        facingRight = true; //draw sprite to right

        lives = maxLives = 3;
        ammoCost = 10;
        throwingKnives = maxAmmo = 30;

        attacks = new ArrayList<rangedAttack>(); //initialise arraylist to add ninja star attacks to
        meleeDamage = 1;
        meleeRange = 26;
        attackDamage = 34;

        try {
            //import spritesheet - try catch used
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/UnhingedNinjaSprite3.gif"));

            sprites = new ArrayList<BufferedImage[]>();
            //initialise arraylist of sprites

            for (int i = 0; i < 8; i++) {
                //split spritesheet into individual sprites for each animation, split into sub images - use embedded for loop as sprite sheet is 2d.
                BufferedImage[] b = new BufferedImage[amount[i]];
                for (int j = 0; j < amount[i]; j++) {
                    b[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                }
                sprites.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //set action to stationary and stationary animation
        animation = new Animation();
        action = STATIONARY;
        animation.setFrames(sprites.get(STATIONARY));
        animation.setDelay(600);

        //initialise music object with jump sound - wav file as java requires uncompressed music file formats
        jumpMusic = new Music("/jump.wav");

    }
    //getters and setters for lives, ammunition and set to attack
    public int getLives() {
        return lives;
    }

    public int getMaxLives() {
        return maxLives;
    }

    public double getThrowingKnives() {
        return throwingKnives;
    }

    public double getMaxAmmo() {
        return maxAmmo;
    }

    public double getNinjaFAmmo() {
        return ninjaFAmmo;
    }

    public double getDashAmmo() {
        return dashAmmo;
    }

    public void setAttack() {
        attack = true;
    }

    public void setMelee() {
        melee = true;
    }

    public void setDash(boolean dash) {
        this.dash = dash;
    }

    public void setNinjaF(boolean ninjaF) {
        this.ninjaF = ninjaF;
    }

    private void getNextPosition() {
        if (left || ((!facingRight) && dash)) {
            //if moving to the left (key pressed) or facing the left, but not clicking left arrow and the user has dashed.
            if (dash) {
                dx -= dashAcceleration;
                //set to dx to -dash acceleration, higher acceleration
            } else {
                dx -= acceleration;
                //otherwise set to normal acceleration
            }
            if (dx < -maxVelocity && !dash) {
                dx = -maxVelocity;
                //if not dashing, and passes limiting value of max velocity, set to max velocity (-ve)
            } else if (dx < -dashMaxVelocity && dash) {
                dx = -dashMaxVelocity;
                //if dashing, and exceeds max dash velocity, set to max dash velocity (-ve)
            }
        } else if (right || ((facingRight) && dash)) {
            //if moving to the right or facing the right and dashing
            if (dash) {
                //if dashing add dash acceleration to dx
                dx += dashAcceleration;
            } else {
                //else add normal acceleration
                dx += acceleration;
            }
            if (dx > maxVelocity && !dash) {
                dx = maxVelocity; //if exceeding max velocity whilst not dashing, set to max velocity
            } else if (dx > dashMaxVelocity && dash) {
                dx = dashMaxVelocity; //if exceeding dash max velocity whilst dashing, set to max dash velocity
            }
        } else {
            //if not pressing left right or dash, and moving, decelerate to 0
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
        }

        //cannot move whilst attacking
        /*if(((action == MELEE) || (action == ATTACK)) && !(jump || fall)){
            dx = 0;
        }*/

        if (jump && !fall && ninjaF) {
            //if jumping and floating, and not falling
            dy = jumpS * 1.15; //set dy to jump value * constant, for increased jump
            jumpMusic.play(); //set music clip for jump to 0 frame and play
            fall = true; //set to falling so doesnt not repeat and falls back
        } else if (jump && !fall) {
            dy = jumpS; //if jump and not floating, and not falling - not multiplied by constant
            fall = true; //set jump music and fall
            jumpMusic.play();
        }
        if (fall) {
            if (dy > 0 && dash) {
                dy += g * 0.35; //g set to 0.35g when dashing, slightly less [vertical acceleration]
            } else if (dy > 0 && ninjaF) {
                dy += g * 0.05; //g set to 0.05g when floating, considerably less [vertical acceleration]
            } else {
                dy += g; //if not dashing or floating
            }
            //limiting cases
            if (dy > 0) jump = false; //if jumping (or falling), jump must be set to false - jump is only for a single update cycle
            if (dy < 0 && !jump) dy += stopJump; //jump
            if (dy > terminalVelocity) dy = terminalVelocity; //prevents from exceeding terminal velocity
        }
    }

    public void update() {
        //System.out.println(throwingKnives);
        //System.out.println(dashAmmo);
        // System.out.println(ninjaFAmmo); test
        jumpMusic.setVolume(SFXvolume); //set volume from database

        dashAmmo += dashRegen;
        ninjaFAmmo += ninjaFRegen;
        if (ninjaF && (ninjaFAmmo > ninjaFCost)) {
            action = NINJAF;
            ninjaFAmmo -= ninjaFCost;
        }
        if (ninjaF && (ninjaFAmmo < ninjaFCost)) {
            action = STATIONARY;
            ninjaF = false;
        }
        if (ninjaFAmmo > ninjaFMax) ninjaFAmmo = ninjaFMax;
        //if(dash) System.out.println("yy");

        if (dash && (dashAmmo > dashCost)) {
            //System.out.println("x");
            action = DASH;
            dashAmmo -= dashCost;
        }
        if (dash && (dashAmmo < dashCost)) {
            action = STATIONARY;
            dash = false;
        }
        if (dashAmmo > maxDash) dashAmmo = maxDash;


        getNextPosition();
        checkCollisionTileMap();
        setPos(xPosTemp, yPosTemp);


        //attack
        throwingKnives += 0.03;
        if (throwingKnives > maxAmmo) {
            throwingKnives = maxAmmo;
        }
        if (attack && action != ATTACK) {
            if (throwingKnives > ammoCost) {
                action = ATTACK;
                throwingKnives -= ammoCost;
                rangedAttack att = new rangedAttack(tilemap, facingRight);
                att.setPos(xPos, yPos);
                attacks.add(att);
                animation.setFrames(sprites.get(ATTACK));
                animation.setDelay(100);
                width = 32;
            }

        }
        for (int i = 0; i < attacks.size(); i++) {
            attacks.get(i).update();
            if (attacks.get(i).isFinished()) {
                attacks.remove(i);
                i--;
            }
        }
        if (melee) {
            if (action != MELEE) {
                action = MELEE;
                animation.setFrames(sprites.get(MELEE));
                animation.setDelay(150);
                width = 32;
            }
        } else if (attack) {
            if (action != ATTACK) {
                action = ATTACK;
                animation.setFrames(sprites.get(ATTACK));
                animation.setDelay(150);
                width = 32;
            }
        } else if (dy > 0) {
            if (dash) {
                if (action != DASH) {
                    action = DASH;
                    animation.setFrames(sprites.get(DASH));
                    animation.setDelay(100);
                    width = 32;
                }
            } else if (action != FALLING) {
                action = FALLING;
                animation.setFrames(sprites.get(FALLING));
                animation.setDelay(100);
                width = 32;
            }
        } else if (dy < 0) {
            if (ninjaF) {
                action = NINJAF;
                animation.setFrames(sprites.get(NINJAF));
                animation.setDelay(100);
                width = 32;
            }
            if (action != JUMPING && !ninjaF) {
                action = JUMPING;
                animation.setFrames(sprites.get(JUMPING));
                animation.setDelay(100);
                width = 32;
            }
        } else if (left || right) {
            if (action != RUNNING) {
                action = RUNNING;
                animation.setFrames(sprites.get(RUNNING));
                animation.setDelay(75);
                width = 32;
            }
        } else {
            if (action != STATIONARY) {
                action = STATIONARY;
                animation.setFrames(sprites.get(STATIONARY));
                animation.setDelay(250);
                width = 32;
            }
        }
        if (ninjaF) {
            if (action != NINJAF) {
                action = NINJAF;
                animation.setFrames(sprites.get(NINJAF));
                animation.setDelay(100);
                width = 32;
            }
        }
        animation.update();

        //direction
        if (action != MELEE && action != ATTACK) {
            if (right) facingRight = true;
            if (left) facingRight = false;
        }
        //stop attack if key released
        if (action == MELEE) {
            if (animation.hasPlayed()) {
                melee = false;
            }
        }
        if (action == ATTACK) {
            if (animation.hasPlayed()) {
                attack = false;
            }
        }

        //check if immunity has finished
        if((System.nanoTime() - immunityBegin > 1500000000) && immunity) immunity = false;
        //if over 20s from last attack, give 3 lives back - check it does not exceed maxLives
        long L = 30000000000L;
        if((System.nanoTime() - immunityBegin > L) && lives!=maxLives){
            lives++;
        }
        if(lives>maxLives) lives=maxLives;
    }

    public void draw(Graphics2D g) {
        setMapPosition();


        for (int i = 0; i < attacks.size(); i++) {
            attacks.get(i).draw(g);
        }

        if (facingRight) {
            g.drawImage(animation.getImage(),
                    (int) (xPos + xPosMap - width / 2),
                    (int) (yPos + yPosMap - height / 2),
                    null);
        } else {
            g.drawImage(animation.getImage(),
                    (int) (xPos + xPosMap - width / 2 + width),
                    (int) (yPos + yPosMap - height / 2),
                    -width,
                    height,
                    null);
        }
    }

    public void checkAttack(ArrayList<Mobs> mobs, Boss boss) {
        //check melee
        //this - hitboxes
        if (melee) {
            if (facingRight) {
                boolean hit = false;
                for (int i = 0; i < mobs.size(); i++) {
                    Mobs e = mobs.get(i);
                    if (e.getx() > xPos && e.getx() < xPos + meleeRange*2 &&
                            e.gety() > yPos - height / 2 && e.gety() < yPos + height / 2) {
                        e.hit(meleeDamage);
                        hit=true;
                        break;
                    }
                }
                //this under div2
                //stop it checking twice using hit parameter - larger range below - big hitbox
                if(!hit && boss.getx()>xPos && boss.getx()<xPos+meleeRange*2 && boss.gety()>yPos-height
                    &&boss.gety() <yPos + height){
                    boss.hit(meleeDamage);
                }
            } else {
                boolean hit=false;
                for (int i = 0; i < mobs.size(); i++) {
                    Mobs e = mobs.get(i);
                    if (e.getx() < xPos && e.getx() > xPos - meleeRange &&
                            e.gety() > yPos - height / 2 && e.gety() < yPos + height / 2) {
                        hit=true;
                        e.hit(meleeDamage);
                        break;
                    }
                }
                if(!hit && boss.getx()<xPos && boss.getx()>xPos-meleeRange*2 && boss.gety()>yPos-height
                        &&boss.gety() <yPos + height) {
                    boss.hit(meleeDamage);
                }
            }

        }
        for(int j=0; j<attacks.size(); j++){
            for(int i=0; i<mobs.size(); i++){
                if(attacks.get(j).intersects(mobs.get(i))){
                    mobs.get(i).hit(attackDamage);
                    attacks.get(j).setHitEntity();
                    //this under

                    attacks.remove(j);
                    j--;
                    break;
                }
            }
            //this under - crashes on anything - whoops - boss extends mobs
            /* if(attacks.get(j).intersects(boss)){
                /*boss.hit(attackDamage);
                attacks.get(j).setHitEntity();
                attacks.remove(j);
                j--;
                System.out.println("hit");

            }
        */
        }
        //check collisions with player and mobs
        for(int i=0; i<mobs.size(); i++){
            if(this.intersects(mobs.get(i)) && !immunity){
                getHit();
            }
        }
    }
    public void getHit(){
        lives--;
        immunityBegin = System.nanoTime();
        immunity=true;

        //provide 1.5s of immunity from further collisions so it only
        //removes 1 life off the player
    }

    public void setSFXvolume(float SFXvolume) {
        this.SFXvolume = SFXvolume;
    }
    public float getSFXvolume(){
        return SFXvolume;
    }
}
