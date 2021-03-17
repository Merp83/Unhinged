package com.PremBhoot.bodies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Death {
    private int xPos, yPos, xPosMap, yPosMap;
    private int width, height;
    private boolean finished;
    private Animation animation;
    private BufferedImage[] sprites;

    public Death(int x, int y){
        xPos = x;
        yPos = y;
        width = 32;
        height = 32;

        try{
            sprites=new BufferedImage[4];
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/poof.gif"));
            for(int i=0; i<sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(i*32, 0, width, height);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(50);

    }
    public void update() {
        animation.update();
        if(animation.hasPlayed()){
            finished=true;
        }
    }
    public boolean isFinished(){ return finished;
    }
    public void setMapPos(int xPosMap, int yPosMap){
        this.xPosMap = xPosMap;
        this.yPosMap = yPosMap;
    }
    public void draw(Graphics2D g){
        g.drawImage(animation.getImage(), xPos +xPosMap -width/2,
                yPos + yPosMap -height/2, null  );
    }
}
