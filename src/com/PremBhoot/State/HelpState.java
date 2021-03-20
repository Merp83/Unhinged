package com.PremBhoot.State;

import com.PremBhoot.TileMap.Background;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class HelpState extends State {
    private Background bg;
    private Color titleColor;
    private Font titleFont;
    private Font font;
    private BufferedImage[] images;

    public HelpState(GameStateManager gsm, String username, String userID){
        this.gsm = gsm;
        images = new BufferedImage[2];
        try {
            bg = new Background("/plain-white-background.jpg", 1);


            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Lady Radical.ttf");
            assert stream != null;
            titleFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(64f);

            titleColor = new Color(255, 0, 120);
            //titleFont = new Font("Century Gothic", Font.PLAIN, 32);

            InputStream stream2 = ClassLoader.getSystemClassLoader().getResourceAsStream("gameovercre1.ttf");
            assert stream2 != null;
            font = Font.createFont(Font.TRUETYPE_FONT, stream2).deriveFont(20f);

            images[0] = ImageIO.read(getClass().getResourceAsStream("/grim.gif"));
            images[1] = ImageIO.read(getClass().getResourceAsStream("/Boss.gif"));

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
    public void draw(Graphics2D g) {
        bg.draw(g);
        g.setFont(titleFont);
        g.setColor(new Color(20, 20, 20));
        g.drawString("Help Page" , 220, 75);

        g.setFont(font);
        g.drawString("Welcome to Unhinged. A platformer game, your aim", 70, 125);
        g.drawString("is to kill  all enemies, and the boss, and do it", 70, 145);
        g.drawString("as quick as you can! You can change settings by", 70, 165);
        g.drawString("going into the settings page - you can change ", 70, 185);
        g.drawString("key binds, audio and visual settings. ", 70, 205);
        g.drawString("These guys on the right are grims, general  ", 70, 225);
        g.drawString("spawning mobs, use F and R (attacks) to kill them.  ", 70, 245);
        g.drawString("This guy is the boss, he is... mysterious  ", 70, 265);
        g.drawString("Click arrow keys to move, T and E are special  ", 70, 285);
        g.drawString("movement abilities, floating and dashing..  ", 70, 305);
        g.drawString("Good luck... [press ESC to get going]", 150, 345);

        g.drawImage(images[0], 570, 189, -32, 32, null);
        g.drawImage(images[0], 580, 189, null);
        g.drawImage(images[1], 550, 250, 96, 96, null);

    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_ESCAPE){
            gsm.setState(GameStateManager.MENUSTATE);
        }
    }

    @Override
    public void keyReleased(int k) {

    }
}
