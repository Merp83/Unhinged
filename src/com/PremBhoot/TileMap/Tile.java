package com.PremBhoot.TileMap;

import java.awt.image.BufferedImage;

public class Tile {
    private BufferedImage image;
    private int type;
    //variables - two types of tiles, normal[can move through], fixed[cannot move through] - barrier
    public static final int NORMAL = 0;
    public static final int FIXED = 1;

    public Tile(BufferedImage image, int type){
        //constructor takes in images and their types
        this.image = image;
        this.type = type;
    }
    public BufferedImage getImage() {
        return image;
    }
    public int getType(){
        return type;
    }
//getters
}
