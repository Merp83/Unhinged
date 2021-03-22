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
    //declare all variables as private = encapsulate
    private boolean deadScreen, completeScreen; //checks if a dead screen or complete screen is required to be drawn - if it has, game comes to pause
    private Font titleFont, endFont; //fonts for level state [used for end of state, death or completion]
    private Color colour; //colour of fonts
    private boolean musicStart; //checks if music has started, if it has, do not play another [caused music to keep playing on update] - lag without
    private Music music; //music clip variable [music class]
    private long currentTime; //for currentTime of level state
    private long timeStart; //system.nanotime of when first level state starters
    private ArrayList<Mobs> mobs; //arraylist of the different mob entities - boss or grims
    private ArrayList<Death> deathAnimations; //to see whether to play death animation - and where to draw
    private Grim grim1, grim2, grim3, grim4, grim5, grim6, grim7;// grim8; grims to add into mobs arraylist
    private Boss boss1; //boss
    private TileMap tileMap; //tilemap needed for checking intersections, setpositions etc.
    private Background bg; //background of game
    private interfacex interfacex; //UI for player - lives, ammo, dash ammo, ninja float ammo
    private Player player; //player variable needed to draw, update etc
    private Connection con; //sql variables for setting volume of clip
    private PreparedStatement pst, pst2, pst3;
    private ResultSet rs, rs2;

    private Float volume, playerSFX; // volume of music and player sound effects (jumping)

    private String username, userID; //taken from constructor used for getting from sql database the audio level [ and to query (update)
    // the new time completion if level is completed]

    public FirstLevelState(GameStateManager gsm, String username, String userID) {
        this.gsm = gsm;
        this.username = username;
        this.userID = userID;
        init(); //calls public init function to start the level
    }

    @Override
    public void init() {

        try{ //try catch to get resources
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
        //begin timer for level

        tileMap = new TileMap(32); //get tileset spritesheet and map of what to draw in each tile space
        tileMap.loadTiles("/UnhingedTileset4.gif");
        tileMap.loadMap("/unhingedlevel1map.map");
        tileMap.setCamera(0, 0); //set position of camera to initial position
        tileMap.setSmoothScroll(0.02); //set smoothscroll value (from killer game programming)

        bg = new Background("/BackgroundLevel1State.gif"); //create background using background class

        player = new Player(tileMap); //create a player and set position
        player.setPos(100, 250);

        interfacex = new interfacex(player, this); //create hud and set completed screens to false
        deadScreen=completeScreen=false;

        //create all the mobs in the correct position in the tilemap and then add to the arraylist
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
            //connect to database and get audio levels from the settings class for the corresponding user - set volume of music to this value
            // then set value of player SFX volume by going into player object and using public method available [set in update() method]
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
            //if music has not started and init has been run, create a new music with the music (music copyright free from
            //free music archive) - converted from mp4 to wav as uncompressed needed for java
            music = new Music("/musicviafreemusicarchive.wav");
            //play the music and set all the volumes using music class created
            music.play();
            musicStart = true;
            music.setVolume(volume);
            player.setSFXvolume(playerSFX);
        }
        player.update(); //update player and camera
        tileMap.setCamera((int) (Panel.WIDTH / 2 - player.getx()), (int)(Panel.HEIGHT / 2));
        //tileMap.setCamera((int) (Panel.WIDTH / 2 - player.getx()), (int)(Panel.HEIGHT / 2 - player.gety()
        // ));
       // grim.update();
        player.checkAttack(mobs, boss1); //check if player has attacked any mobs by checking rectangular intersections

        for(int i=0; i<mobs.size(); i++){
            //update mobs and check if they have died, if they just died add a new death animation and then remove death animaiton once played
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
        //get current time, div by 10^9 due to nano
        currentTime = (System.nanoTime() - timeStart)/1000000000;
        if(player.gety()>370){
            player.getHit();
            player.setPos(600, 50);
            //if player is about to full out of tilemap (from x 750 to 1400) then set to restart and remove player life

        }
        if(player.getLives() ==0 && deadScreen){
            long time = System.nanoTime();
            while(System.nanoTime() - time<3500000000L){
                //if player is dead, show death screen, then pause game, then end state - for 3.5s
            }

            endState();

        }
        if(boss1.isDead() && completeScreen){
            long time = System.nanoTime();
            while(System.nanoTime() - time<3500000000L){
                //if boss is dead, first show completed screen, update level time completed to database, pause for 3.5s, then end state
            }

            endState();
        }
    }

    @Override
    public void draw(Graphics2D g) {
            //clear screen
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Panel.WIDTH, Panel.HEIGHT);

        //draw background, then tilemap (which draws all individual tiles), if player is alive, draw the player and the hud, draw
        //the mob entities, death animations and if player is dead draw deathscreen (and set boolean value to true to end state)
        //if boss is dead, draw complete screen, boolean value set to true to end the state
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
        //errors without, carefully clear all from game music (to stop replaying), mobs and all animations
        //clears memory
        music.stop();
        mobs.clear();
        deathAnimations.clear();
        player.attacks.clear();

    }
    private void endState(){

        clear();//private functions
        // gsm.resetState(GameStateManager.FIRSTLEVELSTATE); redundant
        gsm.setState(GameStateManager.MENUSTATE); //clears and sets state to menu if esc is played or dead
    }




    public long getCurrentTime(){ return currentTime;} //getter for time

    @Override
    public void keyPressed(int k) {

        //key codes for all actions
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
        //ends actions if key is released
        if(k == KeyEvent.VK_LEFT) player.setLeft(false);
        if(k == KeyEvent.VK_RIGHT) player.setRight(false);
        if(k == KeyEvent.VK_UP) player.setUp(false);
        if(k == KeyEvent.VK_DOWN) player.setDown(false);
        if(k == KeyEvent.VK_W) player.setJump(false);
        if(k == KeyEvent.VK_E) player.setDash(false);
        if(k == KeyEvent.VK_T) player.setNinjaF(false);
    }
    private void uploadTime(){
        //upon completion of a level, connect to database and update new time if new time is less
        //than previous time for the user
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
                //System.out.println("upload"); check tool
                String query = "UPDATE leveluser SET time=? WHERE userID =? AND levelid=1";
                pst3 = con.prepareStatement(query);
                pst3.setInt(1, (int) getCurrentTime());
                pst3.setString(2, userID);
                pst3.execute();
                //update new time if less than previous, try catch as connection to database
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
