package com.PremBhoot.bodies;
import java.awt.image.BufferedImage;

public class Animation {
    private BufferedImage[] frames;
    private int currentFrame;
    private long start;
    private long delay;
    private boolean played;

    public void Animation(){
        played = false;
    }
    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        currentFrame = 0;
        start = System.nanoTime();
        played = false;
    }
    public void setDelay(long delay){ this.delay = delay;}
    public void setFrame(int i){ currentFrame = i;}
    public void update(){
        if (delay == -1) return;
        long elapsed = (System.nanoTime() - start) / 1000000;
        if(elapsed > delay){
            currentFrame++;
            start = System.nanoTime();
        }
        if(currentFrame == frames.length){
            currentFrame = 0;
            played = true;
        }
    }
    public int getCurrentFrame(){ return currentFrame;}
    public BufferedImage getImage(){return frames[currentFrame];}
    public boolean hasPlayed(){return played;}

}
