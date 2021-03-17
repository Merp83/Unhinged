package com.PremBhoot.State;


import com.PremBhoot.bodies.Player;
import com.PremBhoot.unhingedgame.Panel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class interfacex {
    private FirstLevelState state;
    private Player player;
    private Font font;
    private Font endFont;
    private Color colour;
    private BufferedImage star;

    public interfacex(Player player, FirstLevelState firstLevelState){
        state = firstLevelState;
        this.player = player;
        colour = new Color(255, 0, 120);
        try{
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
        g.setFont(font);
        g.setColor(colour);
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
        }
        g.drawString(str1, Panel.WIDTH - str1.length()*11 , 18 );
        if(player.getDashAmmo() > 100){
            g.setColor(new Color(10, 150, 10));
        } else if(player.getDashAmmo() > 60){
            g.setColor(new Color(255, 150, 10));
        } else{
            g.setColor(new Color(255, 20 ,20));
        }
        g.drawString(str2, Panel.WIDTH - str1.length()*11, 36);
        if(player.getNinjaFAmmo() > 500){
            g.setColor(new Color(10, 150, 10));
        } else if(player.getNinjaFAmmo() > 300){
            g.setColor(new Color(255, 150, 10));
        } else{
            g.setColor(new Color(255, 20 ,20));
        }
        g.drawString(str3, Panel.WIDTH - str1.length()*11, 54);
        if(state.getCurrentTime()<30){
            g.setColor(new Color(10, 150, 10));
        } else if(state.getCurrentTime()<60){
            g.setColor(new Color(255, 150, 10));
        } else{
            g.setColor(new Color(255, 10, 10));
        }
        g.drawString(str4, Panel.WIDTH - str1.length()*11, 72);


        if(player.getThrowingKnives() > 29.99) {
            g.drawImage(star, 10, 10, null);
            g.drawImage(star, 44, 10, null);
            g.drawImage(star, 78, 10 , null);

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

    }
}
