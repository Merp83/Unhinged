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
        this.username = username;
        this.userID = userID;
        currentChoice = 0;

        settings = new float[2];
        try{

            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/loginsystem", "root", "");
            String query = "SELECT * FROM `settings` WHERE userID=?";
            pst = con.prepareStatement(query);
            pst.setString(1, userID);
            rs = pst.executeQuery();
            rs.next();
            settings[0] = rs.getFloat(2);
            settings[1] = rs.getFloat(3);
            //System.out.println(settings[0]);

        } catch(Exception e){
            e.printStackTrace();
        }

        this.gsm = gsm;
        try {
            bg = new Background("/plain-white-background.jpg", 1);


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

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE){
            gsm.setState(GameStateManager.MENUSTATE);
        }
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

    @Override
    public void keyReleased(int k) {

    }

    @Override
    public void draw(Graphics2D g) {
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
        if(currentChoice == 0){
            settings[0] += 0.005;
            float temp = Math.round(settings[0] * Math.pow(10, 4));
            settings[0] = (float) (temp / Math.pow(10,4));
            if(settings[0] > 1) settings[0] = 0;
        }
        if(currentChoice == 1){
            settings[1] += 0.005;
            float temp = Math.round(settings[1] * Math.pow(10, 4));
            settings[1] = (float) (temp / Math.pow(10,4));

            if(settings[1] > 1) settings[1] = 0;
        }
        try {
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
