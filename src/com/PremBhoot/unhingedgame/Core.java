package com.PremBhoot.unhingedgame;

import javax.swing.*;
import java.util.concurrent.TimeUnit;


public class Core {

    public static void main(String[] args) {
            boolean loggedin=false;
            //stays in loop until logged in (or registered a new account)
            LoginRegistrationManager manager = new LoginRegistrationManager();

            while(loggedin == false){
                        try {
                            TimeUnit.MILLISECONDS.sleep(50);
                            loggedin = manager.getLoggedIn();
                            //causes to use less resources, sleeps.

                            //error - when switching to register, if i switch back to login, doesnt work - have to restart as it uses .getLoggedIn() from the object
                            //instantiated in core, rather than that in registerForm
                        }catch(Exception e){
                            e.printStackTrace();
                        }
            }
         String userID = manager.getUserID();
         String username = manager.getUsername();


        //create JFrame using swing
        JFrame Game = new JFrame("Unhinged");
        Panel panel = new Panel(userID, username);
        Game.add(panel);
        Game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game.setResizable(false);
        Game.pack();
        Game.setVisible(true);




    }
}
