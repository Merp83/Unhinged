package com.PremBhoot.TileMap;


import com.PremBhoot.unhingedgame.GamePanel;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.ImageIO;

public class Background {

    private BufferedImage image;

    //use dx, dy and moveScale to move background.
    // (currently set to 0 in menuState so background is static)
    private double x,y,dx,dy,moveScale;

    public Background(String s, double ms) {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(s));
            moveScale = ms;
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void setPosition(double x, double y) {
        this.x = (x*moveScale) % GamePanel.WIDTH;
        this.y = (y*moveScale) % GamePanel.HEIGHT;
    }
    public void setVector(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
    public void update(){
        x+=dx;
        y+=dy;
    }
    public void draw(Graphics2D g) {

        g.drawImage(image, (int)x, (int)y, null);
        if (x<0) {
            g.drawImage(image, (int)x + GamePanel.WIDTH, (int)y,null);
        }
        if (x>0) {
            g.drawImage(image, (int)x - GamePanel.WIDTH, (int)y,null);
        }

    }
}
