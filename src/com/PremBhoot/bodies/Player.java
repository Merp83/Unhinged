package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;
import com.PremBhoot.State.Music;

import java.awt.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class Player extends Structure {

    private long immunityBegin;
    private boolean immunity;

    private double ninjaFCost;
    private double ninjaFRegen;
    private double ninjaFMax;
    private double ninjaFAmmo;

    private double dashRegen;
    private double dashCost;
    private double dashAmmo;
    private double maxDash;
    private double dashMaxVelocity;
    private double dashAcceleration;

    private int lives;
    private int maxLives;

    private double throwingKnives;
    private double maxAmmo;
    private double ammoCost;
    private int attackDamage;

    private boolean dead;


    private boolean attack;


    public ArrayList<rangedAttack> attacks;

    private boolean melee;
    private int meleeDamage;
    private int meleeRange;
    private Font titleFont;
    private Color colour;

    private boolean dash;
    private boolean ninjaF;

    private ArrayList<BufferedImage[]> sprites;
    private final int[] amount = {2, 4, 1, 2, 4, 2, 2, 4};


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

        facingRight = true;

        lives = maxLives = 3;
        ammoCost = 10;
        throwingKnives = maxAmmo = 30;

        attacks = new ArrayList<rangedAttack>();
        meleeDamage = 1;
        meleeRange = 26;
        attackDamage = 34;

        try {
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/UnhingedNinjaSprite3.gif"));

            sprites = new ArrayList<BufferedImage[]>();


            for (int i = 0; i < 8; i++) {
                BufferedImage[] b = new BufferedImage[amount[i]];
                for (int j = 0; j < amount[i]; j++) {
                    b[j] = spritesheet.getSubimage(j * width, i * height, width, height);
                }
                sprites.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        animation = new Animation();
        action = STATIONARY;
        animation.setFrames(sprites.get(STATIONARY));
        animation.setDelay(600);

        jumpMusic = new Music("/jump.wav");

    }

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
            if (dash) {
                dx -= dashAcceleration;
            } else {
                dx -= acceleration;
            }
            if (dx < -maxVelocity && !dash) {
                dx = -maxVelocity;
            } else if (dx < -dashMaxVelocity && dash) {
                dx = -dashMaxVelocity;
            }
        } else if (right || ((facingRight) && dash)) {
            if (dash) {
                dx += dashAcceleration;
            } else {
                dx += acceleration;
            }
            if (dx > maxVelocity && !dash) {
                dx = maxVelocity;
            } else if (dx > dashMaxVelocity && dash) {
                dx = dashMaxVelocity;
            }
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
        }

        //cannot move whilst attacking
        /*if(((action == MELEE) || (action == ATTACK)) && !(jump || fall)){
            dx = 0;
        }*/

        if (jump && !fall && ninjaF) {
            dy = jumpS * 1.15;
            jumpMusic.play();
            fall = true;
        } else if (jump && !fall) {
            dy = jumpS;
            fall = true;
            jumpMusic.play();
        }
        if (fall) {
            if (dy > 0 && dash) {
                dy += g * 0.35;
            } else if (dy > 0 && ninjaF) {
                dy += g * 0.05;
            } else {
                dy += g;
            }
            if (dy > 0) jump = false;
            if (dy < 0 && !jump) dy += stopJump;
            if (dy > terminalVelocity) dy = terminalVelocity;
        }
    }

    public void update() {
        //System.out.println(throwingKnives);
        //System.out.println(dashAmmo);
        // System.out.println(ninjaFAmmo);
        jumpMusic.setVolume(SFXvolume);

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
