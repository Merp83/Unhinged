package com.PremBhoot.bodies;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Death {
    //variables for position, size, if completed and sprites of death animation
    private int xPos, yPos, xPosMap, yPosMap;
    private int width, height;
    private boolean finished;
    private Animation animation;
    private BufferedImage[] sprites;

    public Death(int x, int y){
        //get variables from constructor
        xPos = x;
        yPos = y;
        width = 32;
        height = 32;

        try{
            //create a new sprite sheet of the image, then split the image (128/32 = 4 animation frames)
            sprites=new BufferedImage[4];
            BufferedImage spritesheet = ImageIO.read(getClass().getResourceAsStream("/poof.gif"));
            for(int i=0; i<sprites.length; i++){
                sprites[i] = spritesheet.getSubimage(i*32, 0, width, height);
            }
        } catch(Exception e){
            e.printStackTrace();
        }
        //use animation class to create an object with the death animations of entities
        animation = new Animation();
        animation.setFrames(sprites);
        animation.setDelay(50);

    }
    public void update() {
        //update the animation / proxy to animation object
        animation.update();
        if(animation.hasPlayed()){
            //check if finished, if have end
            finished=true;
        }
    }
    public boolean isFinished(){ return finished; //getter
    }
    public void setMapPos(int xPosMap, int yPosMap){ //setter
        this.xPosMap = xPosMap;
        this.yPosMap = yPosMap;
    }
    public void draw(Graphics2D g){
        g.drawImage(animation.getImage(), xPos +xPosMap -width/2,
                yPos + yPosMap -height/2, null  ); //draw at correct position
            //use same draw method for all entities, add map distance, subtract dimension (to centre entity at position)
    }
}
