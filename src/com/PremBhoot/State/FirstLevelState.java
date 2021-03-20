package com.PremBhoot.State;

import com.PremBhoot.TileMap.*;
import com.PremBhoot.bodies.*;
import com.PremBhoot.unhingedgame.Panel;
import com.PremBhoot.TileMap.Background;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class FirstLevelState extends State {
    private boolean deadScreen, completeScreen;
    private Font titleFont, endFont;
    private Color colour;
    private boolean musicStart;
    private Music music;
    private long currentTime;
    private long timeStart;
    private ArrayList<Mobs> mobs;
    private ArrayList<Death> deathAnimations;
    private Grim grim1, grim2, grim3, grim4, grim5, grim6, grim7, grim8;
    private Boss boss1;
    private TileMap tileMap;
    private Background bg;
    private interfacex interfacex;
    private Player player;
    private Connection con;
    private PreparedStatement pst, pst2, pst3;
    private ResultSet rs, rs2;

    private Float volume, playerSFX;

    private String username, userID;

    public FirstLevelState(GameStateManager gsm, String username, String userID) {
        this.gsm = gsm;
        this.username = username;
        this.userID = userID;
        init();
    }

    @Override
    public void init() {

        try{
            InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("Lady Radical.ttf");
            assert stream != null;
            titleFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(64f);
            //endFont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(54f);
            colour = new Color(0, 0, 0);
        } catch(Exception e){
            e.printStackTrace();
        }
        musicStart = false;
        timeStart = System.nanoTime();

        tileMap = new TileMap(32);
        tileMap.loadTiles("/UnhingedTileset4.gif");
        tileMap.loadMap("/unhingedlevel1map.map");
        tileMap.setCamera(0, 0);
        tileMap.setSmoothScroll(0.02);

        bg = new Background("/BackgroundLevel1State.gif", 1);

        player = new Player(tileMap);
        player.setPos(100, 250);

        interfacex = new interfacex(player, this);
        deadScreen=completeScreen=false;

        mobs = new ArrayList<Mobs>();
        grim1 = new Grim(tileMap);
        grim2 = new Grim(tileMap);
        grim3 = new Grim(tileMap);
        grim4 = new Grim(tileMap);
        grim5 = new Grim(tileMap);
        grim6 = new Grim(tileMap);
        grim7 = new Grim(tileMap);

        boss1 = new Boss(tileMap);
        grim1.setPos(200, 100);
        grim2.setPos(400, 100);
        grim3.setPos(500, 100);
        grim4.setPos(1800, 100);
        grim5.setPos(2100, 100);
        grim6.setPos(1900, 100);
        grim7.setPos(2400, 100);
        boss1.setPos(2700, 150);


        mobs.add(grim1);
        mobs.add(grim2);
        mobs.add(grim3);
        mobs.add(grim4);
        mobs.add(grim5);
        mobs.add(grim6);
        mobs.add(grim7);

        mobs.add(boss1);


        deathAnimations = new ArrayList<Death>();

        //initially put music in init - as game state manager initialises
        //all states before going into the state, it would play music in
        //menu state, thus added to update function with a boolean checking if it has played.

        try{

            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/loginsystem", "root", "");
            String query = "SELECT * FROM `settings` WHERE userID=?";
            pst = con.prepareStatement(query);
            pst.setString(1, userID);
            rs = pst.executeQuery();

           if(rs.next())
            volume = (float) rs.getFloat(2);
            playerSFX = (float) rs.getFloat(3);
            //System.out.println(settings[0]);

        } catch(Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void update() {
        if(!musicStart){
            music = new Music("/musicviafreemusicarchive.wav");
            music.play();
            musicStart = true;
            music.setVolume(volume);
            player.setSFXvolume(playerSFX);
        }
        player.update();
        tileMap.setCamera((int) (Panel.WIDTH / 2 - player.getx()), (int)(Panel.HEIGHT / 2));
        //tileMap.setCamera((int) (Panel.WIDTH / 2 - player.getx()), (int)(Panel.HEIGHT / 2 - player.gety()
        // ));
       // grim.update();
        player.checkAttack(mobs, boss1);

        for(int i=0; i<mobs.size(); i++){
            mobs.get(i).update();
            //this under
            if(mobs.get(i).isDead()){
                //this under
                deathAnimations.add(new Death(mobs.get(i).getx(), mobs.get(i).gety()));
                mobs.remove(i);
                i--;
            }

        }
        for(int j=0; j<deathAnimations.size(); j++){

            deathAnimations.get(j).update();
            if(deathAnimations.get(j).isFinished()){
                deathAnimations.remove(j);
                j--;
            }
        }
        currentTime = (System.nanoTime() - timeStart)/1000000000;
        if(player.gety()>370){
            player.getHit();
            player.setPos(600, 50);

        }
        if(player.getLives() ==0 && deadScreen){
            long time = System.nanoTime();
            while(System.nanoTime() - time<3500000000L){
                //
            }

            endState();

        }
        if(boss1.isDead() && completeScreen){
            long time = System.nanoTime();
            while(System.nanoTime() - time<3500000000L){
                //
            }

            endState();
        }
    }

    @Override
    public void draw(Graphics2D g) {
            //clear
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Panel.WIDTH, Panel.HEIGHT);

        bg.draw(g);
        tileMap.draw(g);
        if(player.getLives()>0) {
            player.draw(g);
            interfacex.draw(g);
        }

        //grim.draw(g);
        for(int i=0; i<mobs.size(); i++){
            mobs.get(i).draw(g);

            }
        for(int j=0; j<deathAnimations.size(); j++){
            deathAnimations.get(j).setMapPos((int)tileMap.getXPos(),(int) tileMap.getYPos());
            deathAnimations.get(j).draw(g);

            }
        if(player.getLives() == 0){
            g.setColor(colour);
            g.setFont(titleFont);
            g.drawString("You're Dead", 200, Panel.HEIGHT/2);
            deadScreen=true;
        }
        if(boss1.isDead()){
            g.setColor(colour);
            g.setFont(titleFont);
            g.drawString("You Finished in " + getCurrentTime() + " s", 120, Panel.HEIGHT/2);
            completeScreen = true;
            uploadTime();
        }
    }
    private void clear(){
        //errors without, carefully clear all from game
        music.stop();
        mobs.clear();
        deathAnimations.clear();
        player.attacks.clear();

    }
    private void endState(){
        clear();
        gsm.resetState(GameStateManager.FIRSTLEVELSTATE);
        gsm.setState(GameStateManager.MENUSTATE);
    }




    public long getCurrentTime(){ return currentTime;}

    @Override
    public void keyPressed(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(true);
        if(k == KeyEvent.VK_RIGHT) player.setRight(true);
        if(k == KeyEvent.VK_UP) player.setUp(true);
        if(k == KeyEvent.VK_DOWN) player.setDown(true);
        if(k == KeyEvent.VK_UP) player.setJump(true);
        if(k == KeyEvent.VK_E) player.setDash(true);
        if(k == KeyEvent.VK_T) player.setNinjaF(true);
        if(k == KeyEvent.VK_R) player.setMelee();
        if(k== KeyEvent.VK_F) player.setAttack();
        if(k ==KeyEvent.VK_ESCAPE) endState();


    }

    @Override
    public void keyReleased(int k) {
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W) player.setJump(false);
        if(k == KeyEvent.VK_E) player.setDash(false);
        if(k == KeyEvent.VK_T) player.setNinjaF(false);
    }
    private void uploadTime(){
        long time = 999999;
        try{
            //System.out.println(userID);
            //System.out.println(getCurrentTime());
            con= DriverManager.getConnection("jdbc:mysql://localhost:3306/loginsystem", "root", "");
            String query = "SELECT time FROM leveluser WHERE userID=?";
            pst2 = con.prepareStatement(query);
            pst2.setString(1,userID);
            rs2 = pst2.executeQuery();
            rs2.next();


            time = rs2.getInt(1);

        } catch (Exception e){
            e.printStackTrace();
        }
        if(time > getCurrentTime()) {
            try {
                System.out.println("baka");
                String query = "UPDATE leveluser SET time=? WHERE userID =? AND levelid=1";
                pst3 = con.prepareStatement(query);
                pst3.setInt(1, (int) getCurrentTime());
                pst3.setString(2, userID);
                pst3.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
