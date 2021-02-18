package com.PremBhoot.unhingedgame;
import com.PremBhoot.gameState.GameStateManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

@SuppressWarnings(".")
public class GamePanel extends JPanel implements Runnable, KeyListener {
    //dimensions
    public static final int WIDTH = 720;
    public static final int HEIGHT = 405;
    public static final int SCALE = 1;

    //game thread
    private Thread thread;
    private boolean running;
    private int frameRate = 300;
    private long targetTime =  1000/frameRate;


    //image
    private BufferedImage image;
    private Graphics2D g;

    //game state manager
    private GameStateManager gsm;


    public GamePanel(){
        super();
        setPreferredSize(new Dimension((int)WIDTH*SCALE, (int)HEIGHT*SCALE));
        setFocusable(true);
        requestFocus();
    }
    public void addNotify() {
        super.addNotify();
        if (thread == null) {
            thread = new Thread(this);
            addKeyListener(this);
            thread.start();
        }
    }

    private void init(){
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        g = (Graphics2D) image.getGraphics();
        running = true;
        gsm =  new GameStateManager();

    }

    public void run() {
        init();

        long start, elapsed, wait;
        while(running){

            start = System.nanoTime();
            update();
            draw();
            drawToScreen();
            elapsed = System.nanoTime() - start;
            wait = targetTime - elapsed / 1000000;

            try {
                Thread.sleep(wait);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void update(){
        gsm.update();
    }
    private void draw() {
        gsm.draw(g);
    }
    private void drawToScreen(){
        Graphics g2 = getGraphics();
        g2.drawImage(image,0,0,null);
        g2.dispose();
    }


    public void keyTyped(KeyEvent key) {

    }


    public void keyPressed(KeyEvent key) {
        gsm.keyPressed(key.getKeyCode());
    }


    public void keyReleased(KeyEvent key) {
        gsm.keyReleased(key.getKeyCode());
    }


}
