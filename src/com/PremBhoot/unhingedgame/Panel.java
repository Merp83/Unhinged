package com.PremBhoot.unhingedgame;
import com.PremBhoot.State.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

@SuppressWarnings(".")
public class Panel extends JPanel implements Runnable, KeyListener {

    //declare all variables, gamestatemanager, threads, game update rate (target time), width and height of panel
    //game thread
    private Thread thread;
    private boolean running;
    private int frameRate = 200;
    private long targetTime =  1000/frameRate;

    private String username,userID;

    //image
    private BufferedImage image;
    private Graphics2D g;

    //game state manager
    private GameStateManager gsm;

    //dimensions
    public static final int WIDTH = 720;
    public static final int HEIGHT = 384;
    public static final int SCALEFACTOR = 1;
    //dimensions = w*sf, h*sf

    public Panel(String userID, String username){
        //constructor, calls JPanel super() methods, sets size of panel, sets and requests focus of panel
        super();
        setPreferredSize(new Dimension((int)WIDTH*SCALEFACTOR, (int)HEIGHT*SCALEFACTOR));
        setFocusable(true);
        setLayout(null);
        requestFocus();
        //takes parameter values as own attribute values
        this.userID = userID;
        this.username = username;
    }
    public void addNotify() {
        //https://docs.oracle.com/javase/6/docs/api/java/awt/Component.html#addNotify%28%29
        //makes the components of panel displayable and connecting it to the native screen resource
        super.addNotify();
        if (thread == null) {
            //if there are no threads, create a new thread and continue running, using run() concurrent method
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init(){
        //initialise bufferedimages by setting dimension and colour space, graphics using
        // Graphics2D methods and initalise game state manager to organise the states of the game, set the game to running
        //so that the run() concurrent methods runs at targetTime
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        running = true;
        gsm =  new GameStateManager(userID, username);

    }

    public void run() {
        //use private init() method to ensure game goes into while loop after basic components of the game have been initialised, gsm, graphics
        init();

        long start, elapsed, wait;
        while(running){

            start = System.nanoTime();
            //runs 3 fundamental methods, updating[checking new positions, changes to game etc], draw, which gets the images of all the entities to draw
            //, finally drawtoscreen() is a method only in the panel class, which actually draws the entities e.g. background, strings, mobs to the panel
            update();
            draw();
            drawToScreen();
            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;
            //to ensure the game runs at the rate of the target time - implement a wait until the program enters the next update cycle
            //work out wait by subtracting time to complete update, draw and drawtoscreen methods
            if(wait<0) wait=0;
            //error was thrown without - wait could become negative if computer took
            try {
                Thread.sleep(wait);
                //thread sleep in try catch
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update(){
        gsm.update();
        //call the game state manager to update (which then updates the current state in its arraylist)
    }
    private void draw() {
        gsm.draw(g);
        //call the game state manager to draw (which then calls the current state to draw its entities)
    }
    private void drawToScreen(){
        Graphics g2 = getGraphics();
        g2.drawImage(image,0,0,null);
        g2.dispose();
        //get the graphics then draw to screen from pos(0,0)
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //must be implemented as class implements keylistener, not used.
    }

    public void keyPressed(KeyEvent key) {
        gsm.keyPressed(key.getKeyCode());
        //call gsm to get key strokes from the current state and the action as a result of those keycodes
    }


    public void keyReleased(KeyEvent key) {
        gsm.keyReleased(key.getKeyCode());
        //call gsm to get key strokes from the current state and the action as a result of those keycodes
    }
}
