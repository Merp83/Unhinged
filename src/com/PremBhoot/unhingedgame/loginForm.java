package com.PremBhoot.unhingedgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class loginForm extends LoginStates {
    private JFrame login;
    private JPanel pan;
    private JTextField userLoginTextField;
    private JPasswordField passwordTextField;
    private JButton button1, button2;
    private JLabel usernameLabel, passwordLabel;
    private boolean loggedIn;

    private LoginRegistrationManager lrm;

    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;

    private String username;
    private String userID;

    public loginForm(LoginRegistrationManager lrm){
        this.lrm = lrm;

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
        userLoginTextField.setBounds(100, 20, 165, 25);
        userLoginTextField.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(userLoginTextField);

        passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        passwordLabel.setBounds(10, 50, 80, 25);
        pan.add(passwordLabel);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(100, 50, 165, 25);
        passwordTextField.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(passwordTextField);

        button1 = new JButton("Login");
        button1.setBounds(120, 90, 70, 25);
        button1.setFont(new Font("Verdana", Font.PLAIN, 12));
        button1.addActionListener(((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String query = "SELECT * FROM `loginsystem` WHERE username=? and password=?";
                    con= DriverManager.getConnection("jdbc:mysql://localhost:3306/loginsystem", "root", "");
                    pst = con.prepareStatement(query);
                    pst.setString(1, userLoginTextField.getText());

                    Hash hash = new Hash();
                    String hashed = hash.generateHash(passwordTextField.getText());
                    pst.setString(2, hashed);

                    rs=pst.executeQuery();


                    if(rs.next()){
                        loggedIn=true;
                        userID = rs.getString(5);
                        username=rs.getString(1);

                    } else{
                        userLoginTextField.setText("");
                        passwordTextField.setText("");
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        })));
        pan.add(button1);

        button2 = new JButton("Click to register");
        button2.setBounds(100, 160, 130, 25);
        button2.setFont(new Font("Verdana", Font.PLAIN, 12));
        button2.addActionListener((new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerButton(e);
            }
        }));
        pan.add(button2);

        pan.updateUI();


    }
    public void dispose(){
        login.dispose();
    }
    private void registerButton(ActionEvent event){
        lrm.loginToRegistration();
    }
    public boolean getLoggedIn(){
        if(loggedIn){
            dispose();
        }
        return loggedIn;}
    public String getUsername(){return username;}
    public String getUserID(){return userID;}
}
