package com.PremBhoot.gameState;

import com.PremBhoot.TileMap.*;
import com.PremBhoot.bodies.*;
import com.PremBhoot.unhingedgame.GamePanel;
import com.PremBhoot.TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;

public class FirstLevelState extends GameState{

    private TileMap tileMap;
    private Background bg;

    private Player player;

    public FirstLevelState(GameStateManager gsm) {
        this.gsm = gsm;
        init();
    }

    @Override
    public void init() {

        tileMap = new TileMap(32);
        tileMap.loadTiles("/UnhingedTileset2.gif");
        tileMap.loadMap("/unhingedlevel1map.map");
        tileMap.setCamera(0, 0);

        bg = new Background("/BackgroundLevel1State.gif", 1);
        bg.setVector(0, 0);
        player = new Player(tileMap);
        player.setPos(100, 250);

    }

    @Override
    public void update() {
        player.update();
        tileMap.setCamera((int) (GamePanel.WIDTH / 2 - player.getx()), (int)(GamePanel.HEIGHT / 2 - player.gety()));
    }

    @Override
    public void draw(Graphics2D g) {
            //clear
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);

        bg.draw(g);
        tileMap.draw(g);
        player.draw(g);

    }

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_W) player.setJump(true);
        if(k == KeyEvent.VK_E) player.setDash(true);
        if(k == KeyEvent.VK_R) player.setMelee();
        if(k== KeyEvent.VK_F) player.setAttack();


    }

    @Override
    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W) player.setJump(false);
        if(k == KeyEvent.VK_E) player.setDash(false);
    }
}
