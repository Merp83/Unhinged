package com.PremBhoot.unhingedgame;

public abstract class LoginStates {

public abstract void dispose();

public abstract boolean getLoggedIn();

public abstract String getUsername();

public abstract String getUserID();
}
//abstract class with 4 main methods, dispose to close frame and panel, getLoggedIn() to check every
//50ms whether user has logged in and getters