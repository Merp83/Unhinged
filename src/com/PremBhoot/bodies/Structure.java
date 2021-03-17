package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.*;
import com.PremBhoot.unhingedgame.Panel;

import java.awt.*;

public abstract class Structure {

    protected TileMap tilemap;
    protected int tileSize;
    protected double xPosMap;
    protected double yPosMap;

    protected double xPos, yPos, dx, dy;

    protected int width, height;

    //to check for collisions
    protected int collisionWidth;
    protected int collisionheight;

    protected int cRow, cCol;
    protected double xPosDest, yPosDest, xPosTemp, yPosTemp;
    protected boolean tL, tR, bL, bR;

    protected Animation animation;
    protected int action;
    protected int previousAction;
    protected boolean facingRight;

    protected boolean left, right, up, down, jump, fall;

    protected double acceleration, maxVelocity, deceleration, g, terminalVelocity, jumpS, stopJump;

    public Structure (TileMap tm){
        tilemap = tm;
        tileSize = tm.getTileSize();
    }
    //checks is entities can collide

    public boolean intersects(Structure a){

        Rectangle rec1 = getRectangle();
        Rectangle rec2 = a.getRectangle();
        return rec1.intersects(rec2);

    }

    public Rectangle getRectangle(){
        return new Rectangle((int) xPos - collisionWidth, (int) yPos - collisionheight, collisionWidth, collisionheight);
    }

    public void checkCollisionTileMap(){
        cCol = (int) xPos / tileSize;
        cRow = (int) yPos / tileSize;

        xPosDest = xPos + dx;
        yPosDest = yPos + dy;

        xPosTemp = xPos;
        yPosTemp = yPos;

        calculateCornerPos(xPos, yPosDest);
        if(dy< 0){
            if(tL || tR){
                dy = 0;
                yPosTemp = cRow * tileSize + collisionheight / 2;

            } else {
                yPosTemp += dy;
            }
        }
        if(dy>0) {
            if(bL || bR){
                dy = 0;
                fall = false;
                yPosTemp = (cRow + 1) * tileSize - collisionheight / 2;

            } else {
                yPosTemp += dy;
            }
        }

        calculateCornerPos(xPosDest, yPos);
        if(dx< 0){
            if(tL || bL){
                dx = 0;
                xPosTemp = cCol * tileSize + collisionWidth / 2;

            } else {
                xPosTemp += dx;
            }
        }
        if(dx>0) {
            if (tR || bR) {
                dx = 0;
                xPosTemp = (cCol + 1) * tileSize - collisionWidth / 2;
            } else {
                xPosTemp +=dx;
            }
        }
        if(!fall){
            calculateCornerPos(xPos, yPosDest + 1);
            if(!bL && !bR){
                fall=true;
            }
        }
    }
    public int getx(){ return(int) xPos; }
    public int gety(){ return(int) yPos; }
    public int getwidth(){ return width; }
    public int getheight(){ return height; }
    public int getCollisionwidth(){ return collisionWidth; }
    public int getCollisionheight(){ return collisionheight; }

    public boolean notOnScreen(){
        return xPos+xPosMap+width <0 || xPos+xPosMap-width> Panel.WIDTH ||
                yPos + yPosMap + height < 0 || yPos+yPosMap-height> Panel.HEIGHT;
    }

    public void setPos(double x, double y) {
        xPos = x;
        yPos = y;
    }
    public void setVector(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }
    public void setMapPosition(){
        xPosMap = tilemap.getXPos();
        yPosMap = tilemap.getYPos();
    }
    public void setLeft(boolean b){ left = b; }
    public void setRight(boolean b){ right = b;}
    public void setUp(boolean b){ up = b;}
    public void setDown(boolean b){ down = b;}
    public void setJump(boolean b){ jump = b;}



    public void calculateCornerPos(double x, double y){
        int leftTile = (int)(x- collisionWidth / 2) / tileSize;
        int rightTile = (int)(x + collisionWidth / 2 - 1) /tileSize;
        int topTile = (int)(y- collisionheight / 2) / tileSize;
        int bottomTile = (int)(y+ collisionheight / 2 - 1) / tileSize;

        int tl = tilemap.getType(topTile, leftTile);
        int tr = tilemap.getType(topTile, rightTile);
        int bl = tilemap.getType(bottomTile, leftTile);
        int br = tilemap.getType(bottomTile, rightTile);

        tL = tl == Tile.FIXED;
        tR = tr == Tile.FIXED;
        bL = bl == Tile.FIXED;
        bR = br == Tile.FIXED;
    }


}
