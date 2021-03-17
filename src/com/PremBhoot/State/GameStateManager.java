package com.PremBhoot.State;

import java.util.ArrayList;

public class GameStateManager {
    //can also use a stack instead of an arrayList (do not need currentState counter then),
    // simply push new states to the top of the stack - peek to check current game state
    //and pop to remove game state - may use a stack data structure after


    //before arraylist, now array
    public ArrayList<State> states;
    public int currentState;
    // static final int LOGINSTATE = 0;

    public static final int MENUSTATE = 0;
    public static final int FIRSTLEVELSTATE = 2;
    public static final int HELPSTATE = 1;

    private String userID, username;

    public GameStateManager(String userID, String username){
        states = new ArrayList<State>();

       // currentState = LOGINSTATE;
        currentState = MENUSTATE;
      //states.add(new LoginState(this));
        states.add(new MenuState(this, username));
        states.add(new HelpState(this));

        this.userID = userID;
        this.username = username;


    }
    public void setState(int state) {
        currentState = state;
        states.get(currentState).init();
    }
    public void resetState(int state){
       // states.remove(state);
       // states.add(new FirstLevelState(this));
    }
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
