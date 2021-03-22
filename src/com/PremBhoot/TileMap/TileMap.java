package com.PremBhoot.TileMap;

import com.PremBhoot.unhingedgame.Panel;

import java.awt.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class TileMap {
    //variables for the positions, limiting values, rows and column numbers and current row and column
    private double xPosition, yPosition;

    private int xMinimum, xMax, yMinimum, yMax;

    private double smoothScroll;

    private int[][] map;
    private final int tileSize;
    private int numRows;
    private int numColumns;
    private int width;
    private int height;

    private BufferedImage tileset;
    private int numTilesAcross;
    private Tile[][] tiles;

    private int rowOffset;
    private int columnOffset;
    private final int numRowsDraw;
    private final int numColumnsDraw;

    public TileMap(int tileSize) {
        //constructor, takes in tilesize - calculates number of columns and rows to draw (adds 2 due to offset so draws all tiles on screen needed to fill)
        this.tileSize = tileSize;
        numRowsDraw = Panel.HEIGHT / tileSize + 2;
        numColumnsDraw = Panel.WIDTH / tileSize + 2;

    }

    public void loadTiles(String s) {
        try {
            //get tiles spritesheet and split into number of tiles by the tilesize
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];
            //creates array of tiles 2 by number of tiles across

            //subimage to get individual tiles fron the tileset
            BufferedImage subimage;

            for(int column = 0; column < numTilesAcross; column++){
                //initially uses subimage to get the toprow of tiles, each of 32 width
                subimage = tileset.getSubimage(column*tileSize, 0, tileSize, tileSize);
                if(column > 6 && column < 12){
                    tiles[0][column] = new Tile(subimage, Tile.NORMAL);
                    //if the index of the tile is between 7 to 11, set as normal - can walk through
                } else {
                    tiles[0][column] = new Tile(subimage, Tile.FIXED);
                    //otherwise set as an obstacle, fixed
                }
                //second row
                subimage = tileset.getSubimage(column*tileSize, tileSize, tileSize, tileSize);
                if(column==0 || column ==1) {
                    tiles[1][column] = new Tile(subimage, Tile.FIXED);
                    //first and second tiles are obstacles, fixed - others are open, normal
                } else {
                    tiles[1][column] = new Tile(subimage, Tile.NORMAL);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void loadMap(String s){
        try{
            //input stream to get the map file
            InputStream input = getClass().getResourceAsStream(s);
            BufferedReader buffered = new BufferedReader(new InputStreamReader(input));
            numColumns = Integer.parseInt(buffered.readLine());
            numRows = Integer.parseInt(buffered.readLine());
            //gets the number of rows and columns of the map by taking the first two lines of the map file
            //array for the map created
            map = new int[numRows][numColumns];
            width = numColumns * tileSize;
            height = numRows * tileSize;
            //sets limiting values
            xMinimum = Panel.WIDTH - width;
            xMax = 0;
            yMinimum = Panel.HEIGHT - height;
            yMax = 0;

             //regex
            for(int i=0; i<numRows; i++){
                //reads line (after second line), splits them using regex \s+, requiring one or more whitespaces to split up
                String ln = buffered.readLine();
                String[] tk = ln.split("\\s+");
                for (int j = 0; j<numColumns; j++){
                    //embedded for loop to fill map array, turns string to int
                    map[i][j] = Integer.parseInt(tk[j]);
                }
            }
        //try catch due to importing files
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //getters and setters
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }
    public double getXPos(){ return xPosition;
    }
    public double getYPos(){
        return yPosition;
    }
    public int getTileSize(){
        return tileSize;
    }
    public int getType(int row, int column){
        int value = map[row][column];
        return tiles[value/numTilesAcross][value % numTilesAcross].getType();
        //get type of any tileto see whether you can collide or pass through tiles when doing collision detection
    }
    public void setCamera(double xPosition, double yPosition){
        this.xPosition = this.xPosition + smoothScroll*(xPosition - this.xPosition);
        this.yPosition = this.yPosition + smoothScroll*(yPosition - this.yPosition);
        //smoothly move camera - taken from *killer game programming* to make game look smoother, it adjusts the camera by a constant
        //multiplied the difference in the previous and current positions of the player
        correction();
        columnOffset = (int)-this.xPosition/tileSize;
        rowOffset = (int)-this.yPosition/tileSize;
        //offset calculated - offset is if half a tile (or a fraction) needs to be drawn, not a full tile, and also takes away the row offset from the first row
        //e.g. if a user is 3/4 a way through a tile
    }
    public void setSmoothScroll(double d){
        smoothScroll=d;
    }
    //to readjust smooth scroll in different levels if - set to 0.02
    private void correction() {
        if(xPosition < xMinimum){
            xPosition = xMinimum;
        }
        if(xPosition > xMax){
            xPosition = xMax;
        }
        if(yPosition < yMinimum){
            yPosition = yMinimum;
        }
        if(yPosition > yMax){
            yPosition = yMax;
        }
        //limitting values, ensure it doesnt not exceed values

    }
    public void draw(Graphics2D g){
        //draw tiles by using 2 for loops [add additional tiles by using offset]
        for(int row = rowOffset; row<numRowsDraw+rowOffset; row++){
            if(row>= numRows){ break;             }//limiting case to not draw more than the number of rows
            for(int column = columnOffset; column<numColumnsDraw + columnOffset; column++){
                if(column >= numColumns){ //limiting case
                    break;
                } else if (map[row][column] == 0){
                    continue;//if value is 0, do not draw
                }
                int rowcol = map[row][column];
                int rowa = rowcol/numTilesAcross;
                int cola = rowcol %numTilesAcross;
                //get row and column and draw in correct position
                g.drawImage(tiles[rowa][cola].getImage(), (int) xPosition+column *tileSize, (int)yPosition +row*tileSize, null);

            }
        }
    }
}
