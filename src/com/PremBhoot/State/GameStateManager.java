package com.PremBhoot.State;

import java.util.ArrayList;

public class GameStateManager {
    //can also use a stack instead of an arrayList (do not need currentState counter then),
    // simply push new states to the top of the stack - peek to check current game state
    //and pop to remove game state - may use a stack data structure after


    //before arraylist, now array
    public ArrayList<State> states;
    //stores current state
    public int currentState;
    // static final int LOGINSTATE = 0;

    //stores the values for each state - and thus how to change to each one when setting state (or creating level state)
    public static final int MENUSTATE = 0;
    public static final int HELPSTATE = 1;
    public static final int SETTINGSSTATE = 2;
    public static final int SCORESTATE = 3;
    public static final int FIRSTLEVELSTATE = 4;

    //stores userID and username for querying database in states for settings and
    // time to complete levels and drawing users username as a string in menu state
    private String userID, username;

    public GameStateManager(String userID, String username){
        this.userID = userID;
        this.username = username;
        //constructor takes userID and username from the panel class

        states = new ArrayList<State>();
        //initialised arraylist of states - states is an abstract class which *all* states must extend off
       // currentState = LOGINSTATE;
        currentState = MENUSTATE;
      //states.add(new LoginState(this));
        states.add(new MenuState (this, username, userID));
        states.add(new HelpState(this, username, userID));
        states.add(new SettingsState(this, username, userID));
        states.add(new scoreState(this, username, userID));
        //adds all the static states - which take little resources and do not need to reset after leaving
    }
    public void setState(int state) {
        currentState = state;
        states.get(currentState).init();
        //sets the state by initalises them and settings currentstate value to it
    }
    public void resetState(int state){
       // states.remove(state);
       // states.add(new FirstLevelState(this));
        //no longer needed
    }
    //calls upon the current state in the arraylist to do an action e.g. update, draw, register keystrokes.
    public void update() {
        states.get(currentState).update();
    }
    public void draw(java.awt.Graphics2D g) {
        states.get(currentState).draw(g);
    }
    public void keyPressed(int k) {
        states.get(currentState).keyPressed(k);
    }
    public void keyReleased(int k) {
        states.get(currentState).keyReleased(k);
    }

}
