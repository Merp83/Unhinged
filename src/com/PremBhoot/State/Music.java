package com.PremBhoot.State;

import javax.sound.sampled.*;


public class Music {
    private Clip clip;
    //private variable for audio clip
    public Music(String directory){
        //constructor takes path to file as parameter
        try{
            //imports the audio using AudioSystem get audio input stream, audio files must be wav and not mp4 as
            //java does not accept compressed audio types
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(getClass().getResourceAsStream(directory));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            //open the clip

        } catch(Exception e){
            e.printStackTrace();
            //try catch as importing files
        }
    }
    public void play(){
        //start playing the clip (if there is no clip end function), else end the audio clip and start from the beginning
        if (clip==null){
            return;
        }
        stop();
        clip.setFramePosition(0);
        clip.start();
    }
    public void stop(){
        //stop clip for running (temporary)
        if(clip.isRunning()){
            clip.stop();
        }
    }
    public void end(){
        //permanently clear and stop audio clip
        stop();
        clip.close();
    }
    public void setVolume(double level){
        //change audio level using gain control, gain (db) is logarithmic, increases double for each ten fold increase - audio engineering
        //takes parameter of level and converts to db
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float dB = (float) (Math.log(level) / Math.log(10) * 20);
        gainControl.setValue(dB);
    }
}
