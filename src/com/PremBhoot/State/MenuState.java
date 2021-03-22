package com.PremBhoot.State;


import com.PremBhoot.TileMap.Background;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.InputStream;


public class MenuState extends State {
    //variables for graphics, text strings, colours and for username and userID [needed for when creating other states which need userID to query database
    //username used as welcome text]
    private Background bg;
    private String[] options = {"Start", "Settings", "Help", "Scores", "Quit"};
    //5 options to draw which the usercan chose, initialy choice set to start, 0
    private int currentChoice = 0;
    private Color titleColor;
    private Font titleFont;
    private Font font;
    private String username, userID;

    public MenuState( GameStateManager gsm, String username, String userID){
        //get gsm, username and userID from gamestatesmanager as parameters
        this.gsm = gsm;
        try {
            bg = new Background("/BackgroundMenuSingle.png");
            //import background and fonts needed, get colors

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
            //try catch
        }
        this.username = username;
        this.userID = userID;
    }
    public void init(){ //not needed [fundamental method - extended state abstract class]

    }
    public void update(){
        return;
    }
    public void draw (Graphics2D g){
        //draw background, then draw title and options
        bg.draw(g);

        //draw title
        g.setColor(titleColor);
        g.setFont(titleFont);
        g.drawString("Unhinged", 290,75);

        //draw menu options
        g.setFont(font);
        for(int i =0; i < options.length; i++){
            //if selected the option is changed to a darker colour to give visual confirmation of the option selected - else drawn red
            if (i == currentChoice) {
                g.setColor(new Color(119, 0, 98));
            } else {
                g.setColor(new Color(255, 0, 120));
            }
            g.drawString(options[i], 340, 130 + i * 30);
            //draw each line 30 pixels below one another, starting at y 130
        }
        if(username.length() < 40) {
            //if username is short, draw "welcome "--" "
            g.setColor(new Color(255, 0, 120));
            g.drawString("Welcome " + username, 7, 25);
        }


    }
    private void select(){
        //if first choice selected, create and enter new game level
        //if second choice, enter settings
        //if third choice, enter help state
        //if fourth, enter score/leaderboard state
        //if fifth quit game
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
            //use private select method to deterine action(Which state to go into or whether to quit the game)
        }
        if (k == KeyEvent.VK_UP) {
            //if up arrow, decrease option value (go upwards on list) - if at the top and press then go to the bottom [cycle]
            currentChoice--;
            if (currentChoice == -1) {
                currentChoice = options.length - 1;
            }
        }
        if (k == KeyEvent.VK_DOWN) {
            //if down arrow, increase option value (go downwards on list) - if at bottom and pressed then go to top [cycle]
            currentChoice++;
            if (currentChoice == options.length) {
                currentChoice = 0;
            }
        }
    }
    public void keyReleased(int k) {
    //not needed
    }
}
