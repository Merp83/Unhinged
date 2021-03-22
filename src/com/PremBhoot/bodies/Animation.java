package com.PremBhoot.bodies;
import java.awt.image.BufferedImage;

public class Animation {
    //animation class for changing images
    private BufferedImage[] frames;
    private int currentFrame;
    private long start;
    private long delay;
    private boolean played;
    //constructor - sets played to false to allow to play once
    public Animation(){
        played = false;
    }
    public void setFrames(BufferedImage[] frames){
        this.frames = frames;
        currentFrame = 0;
        start = System.nanoTime();
        played = false;
        //gets buffered image as parameter and starts
    }
    public void setDelay(long delay){ this.delay = delay;}
    //delay between changing frame
    public void setFrame(int i){ currentFrame = i;}
    //setter
    public void update(){
        //update, if delay is -1, constant frame -
        //change to new frame if delay has elapsed, if frame is at the end of animation, set played to true
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
    //getter and image
    public int getCurrentFrame(){ return currentFrame;}
    public BufferedImage getImage(){return frames[currentFrame];}//get frame to draw - no direct draw in animation class as excessive parameters to set size and drawe
    //simply changes frame
    public boolean hasPlayed(){return played;}
        //check if animation has played
}
