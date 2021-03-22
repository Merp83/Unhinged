package com.PremBhoot.TileMap;


import com.PremBhoot.unhingedgame.Panel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Background {

    private BufferedImage image;
    //private variables of position of background
        private double x,y;

    public Background(String s) {
        //constructor, takes directory location of the background -
        try {
            //in try catch to get resource, ImageIO methods as part of javax library
            image = ImageIO.read(getClass().getResourceAsStream(s));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void setPosition(double x, double y) {
        //set position, take mod with panel width and height [as position could be greater than panel dimensions]
        this.x = (x) % Panel.WIDTH;
        this.y = (y) % Panel.HEIGHT;
    }

    public void draw(Graphics2D g) {
        //draw images at their position
        g.drawImage(image, (int)x, (int)y, null);
        if (x<0) {
            g.drawImage(image, (int)x + Panel.WIDTH, (int)y,null);
        }
        //if the xposition is negative add panel width
        if (x>0) {
            g.drawImage(image, (int)x - Panel.WIDTH, (int)y,null);
        }
        //if xposition is positive subtract panel width
    }
}
