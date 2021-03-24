package com.PremBhoot.bodies;

import com.PremBhoot.TileMap.*;
import com.PremBhoot.unhingedgame.Panel;

import java.awt.*;

public abstract class Structure {
    //values are protected as abstract class
    protected TileMap tilemap; //takes in tilemap as paramter for collisions
    protected int tileSize; //requires tilesize for collisions
    protected double xPosMap;   //map position of entity
    protected double yPosMap;

    protected double xPos, yPos, dx, dy;    //position and rate of change of position

    protected int width, height; //dimensions

    //to check for collisions
    protected int collisionWidth;
    protected int collisionheight;

    protected int cRow, cCol; //current row and column
    protected double xPosDest, yPosDest, xPosTemp, yPosTemp; //used in check collisions, to set values for the destination
    //using dx and dy and the actual next values for x and y (which will be set in the same update cycle)
    protected boolean tL, tR, bL, bR; //boolean values to check if collided with tiles at top left, top right, bottom left and bottom right
                                        //of entity

    protected Animation animation; //object for the animation of any entity
    protected int action;   //integer values stores the action for each entity, declared in each entity
    protected boolean facingRight;  //used to draw entities facing right or left

    protected boolean left, right, up, down, jump, fall; //actions

    protected double acceleration, maxVelocity, deceleration, g, terminalVelocity, jumpS, stopJump;
    //movement variables, change in x and rate of change of chang

    public Structure (TileMap tm){
        tilemap = tm;   //constructor initialises tm to tilemap of first level state
        tileSize = tm.getTileSize(); //getter
    }
    //checks is entities can collide

    public boolean intersects(Structure a){

        Rectangle rec1 = getRectangle();    //creates rectangles of own entity and another entity (as a parameter),
                                            // if their collisions hitbox intersect, they have "collided"
        Rectangle rec2 = a.getRectangle();
        return rec1.intersects(rec2);

    }

    public Rectangle getRectangle(){
        return new Rectangle((int) xPos - collisionWidth, (int) yPos - collisionheight, collisionWidth, collisionheight);
                                            //get position by subtraction collision hitbox from central position (of subject)
                                            //length and width should be the collision hitbox, which is smaller than main hitbox (or same)
    }

    public void checkCollisionTileMap(){
        cCol = (int) xPos / tileSize;   //get current column and row by dividing by tilesize
        cRow = (int) yPos / tileSize;

        xPosDest = xPos + dx;   //calculate temporary destination if no collisions
        yPosDest = yPos + dy;

        xPosTemp = xPos;    //set temp to current xpos and ypos
        yPosTemp = yPos;

        calculateCornerPos(xPos, yPosDest); //calculate the corner values for the same xposition, with a change in yposition
        if(dy< 0){  //if moving upwards [reminder that 0 denotes the top value, and moves downwards]
            if(tL || tR){   //if collide with top left or top right tiles, set rate of change of y to 0
                dy = 0;
                yPosTemp = cRow * tileSize + collisionheight / 2;   //set position to current row*tilesize (height),
                                                                    //add collision height/2 due to uncertainty of integer division
                                                                    //approximation

            } else {
                yPosTemp += dy; //otherwise ypostemp becomes yposdest (by adding dy)
            }
        }
        if(dy>0) {  //if falling and collides with floor
            if(bL || bR){
                dy = 0; //set change in y to 0
                fall = false;
                yPosTemp = (cRow + 1) * tileSize - collisionheight / 2; //position upwards, approximation due to integer divison

            } else {
                yPosTemp += dy; //if no collision
            }
        }

        calculateCornerPos(xPosDest, yPos); //check x collision
        if(dx< 0){  //if moving left (0 is set to left)
            if(tL || bL){   //if left tiles collide with tile
                dx = 0;     //set velocity to 0
                xPosTemp = cCol * tileSize + collisionWidth / 2; //approximate position (due to integer division) of current row/col

            } else {
                xPosTemp += dx; //if not add dx to xPos
            }
        }
        if(dx>0) {  //check right side collision
            if (tR || bR) {
                dx = 0;
                xPosTemp = (cCol + 1) * tileSize - collisionWidth / 2;
            } else {
                xPosTemp +=dx;
            }
        }
        if(!fall){  //if not falling but no collision with tiles below, set to falling
            calculateCornerPos(xPos, yPosDest + 1);
            if(!bL && !bR){
                fall=true;
            }
        }
    }
    //getters and setters
    public int getx(){ return(int) xPos; }
    public int gety(){ return(int) yPos; }
    public int getwidth(){ return width; }
    public int getheight(){ return height; }
    public int getCollisionwidth(){ return collisionWidth; }
    public int getCollisionheight(){ return collisionheight; }

    public void setPos(double x, double y) {
        xPos = x;
        yPos = y;
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

    public void setchange(double dx, double dy){
        this.dx = dx;
        this.dy = dy;
    }

    public void calculateCornerPos(double x, double y){
        //had to fix + (which was - before), calculated same tile
        int leftTile = (int)(x- collisionWidth / 2) / tileSize;
        int rightTile = (int)(x + collisionWidth / 2 - 1) /tileSize;
        int topTile = (int)(y- collisionheight / 2) / tileSize;
        int bottomTile = (int)(y+ collisionheight / 2 - 1) / tileSize;
        //get x and y values of top [left,right] anf bottom[left,right] tiles
        int tl = tilemap.getType(topTile, leftTile);
        int tr = tilemap.getType(topTile, rightTile);
        int bl = tilemap.getType(bottomTile, leftTile);
        int br = tilemap.getType(bottomTile, rightTile);
        //using tile methods, use tilemap object to get the type (fixed or normal) of the tile at the possition
        tL = tl == Tile.FIXED;  //if tl is fixed, set tL collision to true
        tR = tr == Tile.FIXED;  //if tr is fixed, set tR collision to true
        bL = bl == Tile.FIXED;  //if bl if fixed, set bL collision to true
        bR = br == Tile.FIXED;  //if br is fixed, set bR collision to true
    }


}
