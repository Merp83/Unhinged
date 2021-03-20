package com.PremBhoot.State;


import com.PremBhoot.TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.InputStream;


public class MenuState extends State {

    private Background bg;
    private String[] options = {"Start", "Settings", "Help", "Scores", "Quit"};
    private int currentChoice = 0;
    private Color titleColor;
    private Font titleFont;
    private Font font;
    private String username, userID;

    public MenuState( GameStateManager gsm, String username, String userID){
        this.gsm = gsm;
        try {
            bg = new Background("/BackgroundMenuSingle.png", 1);


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
        this.username = username;
        this.userID = userID;
    }
    public void init(){

    }
    public void update(){
        return;
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
        if(username.length() < 40) {
            g.setColor(new Color(255, 0, 120));
            g.drawString("Welcome " + username, 7, 25);
        }


    }
    private void select(){
        if (currentChoice == 0) {
            gsm.states.add(4, new FirstLevelState(gsm, username, userID));
            gsm.setState(GameStateManager.FIRSTLEVELSTATE);

        }
        if (currentChoice == 1) {
            gsm.setState(GameStateManager.SETTINGSSTATE);
        }
        if (currentChoice == 2) {

            //help
           // gsm.states.add(2, new HelpState(gsm));
            gsm.setState(GameStateManager.HELPSTATE);
        }
        if(currentChoice == 3){
            gsm.setState(GameStateManager.SCORESTATE);
        }
        if (currentChoice == 4) {
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
