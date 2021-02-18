package com.PremBhoot.gameState;


//abstract class, cannot be instantiated, provides a blueprint for other classes (game states) - common functionality across all related classes and forces to use default methods
//which are included below
public abstract class GameState {
    protected GameStateManager gsm;
    public abstract void init();


    public abstract void update();


    public abstract void draw(java.awt.Graphics2D g);


    public abstract void keyPressed(int k);


    public abstract void keyReleased(int k);


}
