package com.PremBhoot.TileMap;

import com.PremBhoot.unhingedgame.Panel;

import java.awt.*;
import java.awt.image.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

public class TileMap {
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
        this.tileSize = tileSize;
        numRowsDraw = Panel.HEIGHT / tileSize + 2;
        numColumnsDraw = Panel.WIDTH / tileSize + 2;
        smoothScroll = 0.08;
    }

    public void loadTiles(String s) {
        try {
            tileset = ImageIO.read(getClass().getResourceAsStream(s));
            numTilesAcross = tileset.getWidth() / tileSize;
            tiles = new Tile[2][numTilesAcross];

            //this under
            BufferedImage subimage;
            for(int column = 0; column < numTilesAcross; column++){
                subimage = tileset.getSubimage(column*tileSize, 0, tileSize, tileSize);
                if(column > 6 && column < 12){
                    tiles[0][column] = new Tile(subimage, Tile.NORMAL);
                } else {
                    tiles[0][column] = new Tile(subimage, Tile.FIXED);
                }

                subimage = tileset.getSubimage(column*tileSize, tileSize, tileSize, tileSize);
                if(column==0 || column ==1) {
                    tiles[1][column] = new Tile(subimage, Tile.FIXED);
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
            InputStream input = getClass().getResourceAsStream(s);
            BufferedReader buffered = new BufferedReader(new InputStreamReader(input));
            numColumns = Integer.parseInt(buffered.readLine());
            numRows = Integer.parseInt(buffered.readLine());

            map = new int[numRows][numColumns];
            width = numColumns * tileSize;
            height = numRows * tileSize;

            xMinimum = Panel.WIDTH - width;
            xMax = 0;
            yMinimum = Panel.HEIGHT - height;
            yMax = 0;

             //regex
            for(int i=0; i<numRows; i++){
                String ln = buffered.readLine();
                String[] tk = ln.split("\\s+");
                for (int j = 0; j<numColumns; j++){
                    map[i][j] = Integer.parseInt(tk[j]);
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }
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
    }
    public void setCamera(double xPosition, double yPosition){
        this.xPosition = this.xPosition + smoothScroll*(xPosition - this.xPosition);
        this.yPosition = this.yPosition + smoothScroll*(yPosition - this.yPosition);
        correction();
        columnOffset = (int)-this.xPosition/tileSize;
        rowOffset = (int)-this.yPosition/tileSize;
    }
    public void setSmoothScroll(double d){
        smoothScroll=d;
    }
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

    }
    public void draw(Graphics2D g){
        for(int row = rowOffset; row<numRowsDraw+rowOffset; row++){
            if(row>= numRows){
                break;
            }
            for(int column = columnOffset; column<numColumnsDraw + columnOffset; column++){
                if(column >= numColumns){
                    break;
                } else if (map[row][column] == 0){
                    continue;
                }
                int rowcol = map[row][column];
                int rowa = rowcol/numTilesAcross;
                int cola = rowcol %numTilesAcross;
                g.drawImage(tiles[rowa][cola].getImage(), (int) xPosition+column *tileSize, (int)yPosition +row*tileSize, null);

            }
        }
    }
}
