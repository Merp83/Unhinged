package com.PremBhoot.unhingedgame;

import javax.swing.*;


public class Core {
    public static void main(String[] args) {


        //create JFrame using swing
        JFrame Game = new JFrame("Unhinged");
        Game.setContentPane(new GamePanel());
        Game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game.setResizable(true);
        Game.pack();
        Game.setVisible(true);




    }
}
