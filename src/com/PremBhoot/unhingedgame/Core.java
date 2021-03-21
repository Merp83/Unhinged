package com.PremBhoot.unhingedgame;

import javax.swing.*;
import java.util.concurrent.TimeUnit;


public class Core {

    public static void main(String[] args) {
            //boolean value which checks whether in the login screens if the user has logged in
            boolean loggedin=false;
            //stays in loop until logged in (or registered a new account)
            LoginRegistrationManager manager = new LoginRegistrationManager();
            //create object of lrm, used to organise whether the user is on a login screen or registration screen

            //while loop that checks every 50s if the user has logged in
            while(loggedin == false){
                        try {
                            TimeUnit.MILLISECONDS.sleep(50);
                            loggedin = manager.getLoggedIn();
                            //causes to use less resources, sleeps.

                            //error - when switching to register, if i switch back to login, doesnt work - have to restart as it uses .getLoggedIn() from the object
                            //instantiated in core, rather than that in registerForm - fixed using lrm
                        }catch(Exception e){
                            e.printStackTrace();
                        }
            }
         String userID = manager.getUserID();
         String username = manager.getUsername();
         //gets the userID and username from the lrm (which connects to the login screen) used for other components of game such as getting high scores, user settingd

        //create JFrame using swing - with title unhinged.
        JFrame Game = new JFrame("Unhinged");
        Panel panel = new Panel(userID, username);
        //create panel object, takes userID and username as parameters, panel is added to the jframe, basic settings of the jframe are completed such as making visible
        Game.add(panel);
        Game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Game.setResizable(false);
        Game.pack();
        Game.setVisible(true);




    }
}
