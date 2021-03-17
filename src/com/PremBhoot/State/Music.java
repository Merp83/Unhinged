package com.PremBhoot.State;

import javax.sound.sampled.*;


public class Music {
    private Clip clip;
    public Music(String directory){
        try{
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(directory));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);

        } catch(Exception e){
            e.printStackTrace();
        }
    }
    public void play(){
        if (clip==null){
            return;
        }
        stop();
        clip.setFramePosition(0);
        clip.start();
    }
    public void stop(){
        if(clip.isRunning()){
            clip.stop();
        }
    }
    public void end(){
        stop();
        clip.close();
    }
    public void setVolume(double level){
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(level) / Math.log(10) * 20);
        gainControl.setValue(dB);
    }
}
