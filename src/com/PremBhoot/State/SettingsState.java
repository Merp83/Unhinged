package com.PremBhoot.State;

import com.PremBhoot.TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SettingsState extends State{
    //variables for background and text and sql querying/connection
    private Background bg;
    private String[] options = {"Music Volume" , "SFX Volume"};
    private float[] settings;
    private int currentChoice = 0;
    private Color titleColor;
    private Font titleFont;
    private Font font;
    private String username, userID;

    private Connection con;
    private PreparedStatement pst, pst2;
    private ResultSet rs;

    public SettingsState(GameStateManager gsm, String username, String userID){
        //get username and userID from gsm
        this.username = username;
        this.userID = userID;
        currentChoice = 0;
        //option is 0, first value [play[
        //new array for audio music volume and SFX volume
        settings = new float[2];
        try{
            //try catch - connect to database and query the settings from the settings table [where USERID matches]
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/loginsystem", "root", "");
            String query = "SELECT * FROM `settings` WHERE userID=?";
            pst = con.prepareStatement(query);
            pst.setString(1, userID);
            rs = pst.executeQuery();
            //create prepared statement and query into resultset variable
            rs.next();
            settings[0] = rs.getFloat(2);
            settings[1] = rs.getFloat(3);
            //get values from the result set for settigns [rs.next() needed to open result set - error otherwise]
            //System.out.println(settings[0]);

        } catch(Exception e){
            e.printStackTrace();
            //print if there us an exception
        }
        //set gsm variable to parameter in constructor
        this.gsm = gsm;
        try {
            //use background class to set a plain background
            bg = new Background("/plain-white-background.jpg");

            //import fonts with size 54 and 24 respectively - use input stream class to import
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Lady Radical.ttf");
            assert stream != null;
            titleFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(54f);

            //titleColor = new Color(255, 0, 120);
            //titleFont = new Font("Century Gothic", Font.PLAIN, 32);

            InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("gameovercre1.ttf");
            assert stream2 != null;
            font = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(24f);
            //try catch for imprting
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void init() {
//@override polymorphism - nothing needs to be initialised or updates
    }

    @Override
    public void update() {

    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE){
            gsm.setState(GameStateManager.MENUSTATE);
            //if escape is press return to menu state
        }
        if (k == KeyEvent.VK_ENTER) {
            select();
            //run private method select if enter is pressed - increases/decrease audio volume
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

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void draw(Graphics2D g) {
        //draw background, settings title and then draw the strings audio and sfx volume (with their respective volumes in the database)
        bg.draw(g);
        g.setColor(new Color(255, 0, 120));
        g.setFont(titleFont);
        g.drawString("Settings", 280, 65);
        g.setFont(font);
        for(int i = 0; i< options.length;i++){
            if(i==currentChoice){
                g.setColor(new Color(120, 20, 120));
            } else{
                g.setColor(new Color(255, 0, 120));
            }
            g.drawString(options[i], 140, 120+26*i);
            g.drawString(String.valueOf(settings[i]), 450, 120+26*i);
        }

    }
    private void select(){
        //method uses when enter is pressed on an option
        if(currentChoice == 0){
            //if on audio music volume and press enter, add 0.005 (max 1.0) - round due to floating point addition error
            //which is part of java, to 4 d.p. - if pressed after volume is greater than 1, set volume to 0
            settings[0] += 0.005;
            float temp = Math.round(settings[0] * Math.pow(10, 4));
            settings[0] = (float) (temp / Math.pow(10,4));
            if(settings[0] > 1) settings[0] = 0;
        }
        if(currentChoice == 1){
            //if on sfx volume and press enter, increment by 0.005 - round due to floating point error
            //if pressed when volume is greater than 1, set to 0 [cycle]
            settings[1] += 0.005;
            float temp = Math.round(settings[1] * Math.pow(10, 4));
            settings[1] = (float) (temp / Math.pow(10,4));

            if(settings[1] > 1) settings[1] = 0;
        }
        //try catch for updating database values
        try {
            //go into settings table and update new volume settings as they have been changed in select() method
            String query = "UPDATE settings SET volumeMusic=?, volume=? WHERE userID =?";
            pst2 = con.prepareStatement(query);
            pst2.setFloat(1, settings[0]);
            pst2.setFloat(2, settings[1]);
            pst2.setString(3, userID);
            pst2.execute();

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
