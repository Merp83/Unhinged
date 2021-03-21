package com.PremBhoot.unhingedgame;

import java.util.ArrayList;

public class LoginRegistrationManager {
    //login registration manager class- uses similar methodology to game state manager
    private ArrayList<LoginStates> loginRegistration;
    //create private arraylist of the current state
    public LoginRegistrationManager(){
        //upon game launch initialises arraylist, sets current state to login state
        loginRegistration = new ArrayList<LoginStates>();
        loginRegistration.add(new loginForm(this));
    }
    public void loginToRegistration(){
        //method used in loginForm class to change the window to the registerForm, disposes of the currentUI
        //and then adds register state to arraylist, followed by removing loginstate, in this order
        //so in the core class when requesting .getLoggedIn, there is always a state in the arraylist, even for a split second.
        loginRegistration.get(0).dispose();
        loginRegistration.add(new registerForm(this));
        loginRegistration.remove(0);

    }
    public void registrationToLogin(){
        //method used in registerForm class to change the window to the loginForm, disposes of the currentUI
        //and then adds login state to arraylist, followed by removing registerstate, in this order
        //so in the core class when requesting .getLoggedIn, there is always a state in the arraylist, even for a split second.
        loginRegistration.get(0).dispose();
        loginRegistration.add(new loginForm(this));
        loginRegistration.remove(0);

    }
    //more methods required by the abstract class loginStates to check whether logged in, and to get the username and userID once logged in for other parts
    //of the game
    public boolean getLoggedIn(){
       return loginRegistration.get(0).getLoggedIn();
    }
    public String getUsername(){return loginRegistration.get(0).getUsername();}
    public String getUserID(){return loginRegistration.get(0).getUserID();}
}
