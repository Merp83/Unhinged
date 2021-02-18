package com.PremBhoot.gameState;

import java.util.ArrayList;

public class GameStateManager {
    //can also use a stack instead of an arrayList (do not need currentState counter then),
    // simply push new states to the top of the stack - peek to check current game state
    //and pop to remove game state - may use a stack data structure after

    private ArrayList<GameState> gameStates;
    private  int currentState;
    public static final int MENUSTATE = 0;
    public static final int FIRSTLEVELSTATE = 1;

    public GameStateManager(){
        gameStates = new ArrayList<GameState>();

        currentState = MENUSTATE;
        gameStates.add(new MenuState(this));
        gameStates.add(new FirstLevelState(this));
    }

    public void setState(int state) {
        currentState = state;
        gameStates.get(currentState).init();
    }
    public void update() {
        gameStates.get(currentState).update();
    }
    public void draw(java.awt.Graphics2D g) {
        gameStates.get(currentState).draw(g);
    }
    public void keyPressed(int k) {
        gameStates.get(currentState).keyPressed(k);
    }
    public void keyReleased(int k) {
        gameStates.get(currentState).keyReleased(k);
    }
}
