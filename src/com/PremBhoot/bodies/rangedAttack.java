package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.TileMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class rangedAttack extends Structure{
    private boolean hitEntity, finished;
    private BufferedImage[] attack, hit;
    private double totalDistance;


    public rangedAttack(TileMap tm, boolean facingRight){
        super(tm);
        g=0.3;
        terminalVelocity=3;
        dy=g;
        acceleration = 2;
        width = 32;
        height = 32;
        collisionWidth = 12;
        collisionheight = 20;
        if(facingRight){
            dx=acceleration;
        } else{
            dx=-acceleration;
        }
        try{
            BufferedImage spriteSheet = ImageIO.read(getClass().getResourceAsStream("/AttackSprite.gif"));
            attack = new BufferedImage[3];
            hit = new BufferedImage[3];
            for(int i = 0; i<3; i++){
                attack[i] = spriteSheet.getSubimage(i*width, 0, width, height);
                hit[i] = spriteSheet.getSubimage(i*width, height, width, height);
            }
            animation = new Animation();
            animation.setFrames(attack);
            animation.setDelay(50);


        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void setHitEntity(){
        hitEntity = true;
        animation.setFrames(hit);
        animation.setDelay(50);
        animation.update();
        dx=0;
    }
    public boolean isFinished(){return finished;}
    public void update(){
        checkCollisionTileMap();
        setPos(xPosTemp, yPosTemp);

        if(dx==0 && !hitEntity){
            setHitEntity();

        } else if(totalDistance>1200){
            setHitEntity();
            finished = true;
        }
        animation.update();
        if(hitEntity && animation.hasPlayed()){
            finished = true;
        }

        totalDistance += Math.sqrt(Math.pow(dx,2) + Math.pow(dy, 2));
    }
    public void draw(Graphics2D g){
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
