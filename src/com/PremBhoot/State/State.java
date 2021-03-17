package com.PremBhoot.State;


//abstract class, cannot be instantiated, provides a blueprint for other classes (game states) - common functionality across all related classes and forces to use default methods
//which are included below
public abstract class State {
    protected GameStateManager gsm;
    public abstract void init();
    public abstract void update();
    public abstract void keyPressed(int c);
    public abstract void keyReleased(int c);
    public abstract void draw(java.awt.Graphics2D g);
    }
