package com.PremBhoot.unhingedgame;

import java.util.ArrayList;

public class LoginRegistrationManager {
    private ArrayList<LoginStates> loginRegistration;
    public LoginRegistrationManager(){
        loginRegistration = new ArrayList<LoginStates>();
        loginRegistration.add(new loginForm(this));
    }
    public void loginToRegistration(){
        loginRegistration.get(0).dispose();
        loginRegistration.add(new registerForm(this));
        loginRegistration.remove(0);

    }
    public void registrationToLogin(){
        loginRegistration.get(0).dispose();
        loginRegistration.add(new loginForm(this));
        loginRegistration.remove(0);

    }
    public boolean getLoggedIn(){
       return loginRegistration.get(0).getLoggedIn();
    }
    public String getUsername(){return loginRegistration.get(0).getUsername();}
    public String getUserID(){return loginRegistration.get(0).getUserID();}
}
