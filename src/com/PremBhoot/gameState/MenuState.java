package com.PremBhoot.gameState;


import com.PremBhoot.TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.InputStream;


public class MenuState extends GameState{

    private Background bg;
    private String[] options = {"Start", "Settings", "Help", "Quit"};
    private int currentChoice = 0;
    private Color titleColor;
    private Font titleFont;
    private Font font;

    public MenuState( GameStateManager gsm){
        this.gsm = gsm;
        try {
            bg = new Background("/BackgroundMenuSingle.png", 1);
            bg.setVector(0, 0);

            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Lady Radical.ttf");
            assert stream != null;
            titleFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(54f);

            titleColor = new Color(255, 0, 120);
            //titleFont = new Font("Century Gothic", Font.PLAIN, 32);

            InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("gameovercre1.ttf");
            assert stream2 != null;
            font = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(24f);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void init(){

    }
    public void update(){
        bg.update();
    }
    public void draw (Graphics2D g){
        //draw bg
        bg.draw(g);

        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Unhinged", 290,75);

        //draw menu options
        g.setFont(font);
        for(int i =0; i < options.length; i++){
            if (i == currentChoice) {
                g.setColor(new Color(119, 0, 98));
            } else {
                g.setColor(new Color(255, 0, 120));
            }
            g.drawString(options[i], 340, 130 + i * 30);
        }


    }
    private void select(){
        if (currentChoice == 0) {
            gsm.setState(GameStateManager.FIRSTLEVELSTATE);

        }
        if (currentChoice == 1) {
            //settings
        }
        if (currentChoice == 2) {
            //help
        }
        if (currentChoice == 3) {
            System.exit(0);
        }
    }
    public void keyPressed(int k) {
        if (k == KeyEvent.VK_ENTER) {
            select();
        }
        if (k == KeyEvent.VK_UP) {
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if (k == KeyEvent.VK_DOWN) {
            currentChoice++;
            if (currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k) {

    }
}
