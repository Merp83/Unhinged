package com.PremBhoot.State;


import com.PremBhoot.bodies.Player;
import com.PremBhoot.unhingedgame.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class interfacex {
    //variables including the player, the first level state (to use getters) and fonts/images
    private FirstLevelState state;
    private Player player;
    private Font font;
    private Font endFont;
    private Color colour;
    private BufferedImage star;

    public interfacex(Player player, FirstLevelState firstLevelState){
        //take parameters as own variables
        state = firstLevelState;
        this.player = player;
        colour = new Color(255, 0, 120);
        try{
            //try catch to import image of ninja star to indiciate number of stars left and fonts
            star = ImageIO.read(getClass().getResourceAsStream("/star2.gif"));

            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Lady Radical.ttf");
            assert stream != null;
            endFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(54f);

            InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("gameovercre1.ttf");
            assert stream2 != null;
            font = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(18f);
        } catch(Exception e){
            e.printStackTrace();
        }

    }
    public void draw(Graphics2D g){
        //set font and colour
        g.setFont(font);
        g.setColor(colour);
        //creates strings of the player lives, the amount of dash ability the player has (set to 150 thus div 15 = 10)
        //amount of ninja floating abilities, (set to 750, div 75 = 10)
        //get time elapsed in level from firstlevelstate cbject
        String str1 = "Lives   " + player.getLives();
        String str2 = "Dash   " + (int)player.getDashAmmo()/15;
        String str3 = "Float   " + (int)player.getNinjaFAmmo()/75;
        String str4 = "Time   " + (int)state.getCurrentTime();
        switch(player.getLives()) {
            case 0:
                g.setColor(new Color(255, 20, 20));
                break;
            case 1:
                g.setColor(new Color(255, 20, 120));
                break;
            case 2:
                g.setColor(new Color(255, 150, 10));
            case 3:
                g.setColor(new Color(10, 150, 10));
                //if player lives are max (3), colour is green
                //if medium (2), colour is amber
                //if low(1), colour is red
                //adds visual indicators for player
        }
        g.drawString(str1, Panel.WIDTH - str1.length()*11 , 18 );
        if(player.getDashAmmo() > 100){
            g.setColor(new Color(10, 150, 10));
        } else if(player.getDashAmmo() > 60){
            g.setColor(new Color(255, 150, 10));
        } else{
            g.setColor(new Color(255, 20 ,20));
            //sets colour of dash, if high - green, if medium - amber, if low - red
        }
        g.drawString(str2, Panel.WIDTH - str1.length()*11, 36);
        //dash string drawn
        if(player.getNinjaFAmmo() > 500){
            g.setColor(new Color(10, 150, 10));
        } else if(player.getNinjaFAmmo() > 300){
            g.setColor(new Color(255, 150, 10));
        } else{
            g.setColor(new Color(255, 20 ,20));
        }
        g.drawString(str3, Panel.WIDTH - str1.length()*11, 54);
        //sets colour of ninja float, if high - green, if medium - amber, if low - red
        if(state.getCurrentTime()<30){
            g.setColor(new Color(10, 150, 10));
        } else if(state.getCurrentTime()<60){
            g.setColor(new Color(255, 150, 10));
        } else{
            g.setColor(new Color(255, 10, 10));
        }
        //sets colour of time, if time is low (faster completition) green, otherwise orange then red at 30,60 intervals
        g.drawString(str4, Panel.WIDTH - str1.length()*11, 72);

        //draws 3 full ninjastars/knives if max ammo
        if(player.getThrowingKnives() > 29.99) {
            g.drawImage(star, 10, 10, null);
            g.drawImage(star, 44, 10, null);
            g.drawImage(star, 78, 10 , null);

        //if ammo between 20-30 draws 2 full ninja star knives and 1 half opacity knife
        } else if(player.getThrowingKnives()> 19.99){
            g.drawImage(star, 10, 10, null);
            g.drawImage(star, 44, 10, null);
            //float alpha = (float) ((player.getThrowingKnives() - 20)) / 10; //draw half transparent
            //if (alpha<0) alpha=0;
            float alpha= (float) 0.5;

            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
            g.setComposite(ac);
            g.drawImage(star, 78, 10, null);
            alpha=1;
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
            g.setComposite(ac);
        //if ammo between 9-10, draws 1 full ninja star knive, 2 half opacity
        } else if(player.getThrowingKnives()>9.99){
            g.drawImage(star, 10, 10, null);
            float alpha = (float) 0.5; //draw half transparent
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
            g.setComposite(ac);
            g.drawImage(star, 44, 10, null);
            g.drawImage(star, 78, 10, null);
            alpha=1;
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
            g.setComposite(ac);
        //draws 3 half opacity ninja stars
        } else{
            float alpha = (float) 0.5; //draw half transparent
            AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
            g.setComposite(ac);
            g.drawImage(star, 10, 10, null);
            g.drawImage(star, 44, 10, null);
            g.drawImage(star, 78, 10, null);
            alpha=1;
            ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha);
            g.setComposite(ac);
        }

    }
    public void update(){
    //does not update, all done in draw.
    }
}
