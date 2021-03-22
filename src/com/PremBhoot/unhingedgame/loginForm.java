package com.PremBhoot.unhingedgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class loginForm extends LoginStates {
    //declare all variables as private to encapsulate, including JComponents such as text, textfields, whether user has logged in
    //and sql connections to check in database and query username and password
    private JFrame login;
    private JPanel pan;
    private JTextField userLoginTextField;
    private JPasswordField passwordTextField;
    private JButton button1, button2;
    private JLabel usernameLabel, passwordLabel;
    private boolean loggedIn;

    private LoginRegistrationManager lrm;
    //needed for requesting username, userID after successful login and checking whether logged in - also for organisation of states

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;
    //sql variables

    private String username;
    private String userID;
    //get username and userID and release to rest of game upon successful login

    public loginForm(LoginRegistrationManager lrm){
        this.lrm = lrm;

        //commands create JFrame and Jpanel, sets all buttons, text and textfields to correct position, sets fonts aswell.
        loggedIn = false;
        login = new JFrame("Unhinged Login");
        login.setSize(400, 400);
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setVisible(true);
        login.setResizable(false);


        pan = new JPanel();
        pan.setLayout(null);
        login.add(pan);

        usernameLabel = new JLabel("Username");
        usernameLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        usernameLabel.setBounds(10, 20, 80, 25);
        pan.add(usernameLabel);

       userLoginTextField = new JTextField();
        userLoginTextField.setBounds(100, 20, 205, 25);
        userLoginTextField.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(userLoginTextField);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        passwordLabel.setBounds(10, 50, 80, 25);
        pan.add(passwordLabel);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(100, 50, 205, 25);
        passwordTextField.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(passwordTextField);

        button1 = new JButton("Login");
        button1.setBounds(120, 90, 70, 25);
        button1.setFont(new Font("Verdana", Font.PLAIN, 12));
        //action for login button
        button1.addActionListener(((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    //query connects to database, queries with username and hashed password, if there is a match,
                    //user is logged in (if there is a result in rs.next() loggedIN set to true and userID and username taken in
                    String query = "SELECT * FROM `loginsystem` WHERE username=? and password=?";
                    con= DriverManager.getConnection("jdbc:mysql://localhost:3306/loginsystem", "root", "");
                    //connection to database loginsystem - username root, no password
                    pst = con.prepareStatement(query);
                    //prepared statement initialised using connection and query
                    pst.setString(1, userLoginTextField.getText());
                    //set first parameter username to the value in the username field

                    Hash hash = new Hash();
                    String hashed = hash.generateHash(passwordTextField.getText());
                    //set second parameter password to the hashed password using hash class
                    pst.setString(2, hashed);

                    rs=pst.executeQuery();


                    if(rs.next()){
                        loggedIn=true;
                        userID = rs.getString(5);
                        username=rs.getString(1);

                    } else{
                        userLoginTextField.setText("");
                        passwordTextField.setText("");
                        //if there is no result empty login and password field, do not login in
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                    //all in try catch as connecting to database (and using hash class which uses Message Digest)
                }

            }
        })));
        pan.add(button1);
        //register button
        button2 = new JButton("Click to register");
        button2.setBounds(100, 160, 130, 25);
        button2.setFont(new Font("Verdana", Font.PLAIN, 12));
        button2.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton(e);
            }
            //calls private method to switch to register button, using LRM
        }));
        pan.add(button2);

        pan.updateUI();


    }
    public void dispose(){
        login.dispose();
    }
    //method required as loginForm extends LoginStates

    private void registerButton(ActionEvent event){
        lrm.loginToRegistration();
    }
    //calls LoginRegistrationManager method to swap to registrationForm

    public boolean getLoggedIn(){
        if(loggedIn){
            dispose();
        }
        //getLoggedIN, if true dispose of jframe and return value
        return loggedIn;}
    public String getUsername(){return username;}
    public String getUserID(){return userID;}
    //getters
}
