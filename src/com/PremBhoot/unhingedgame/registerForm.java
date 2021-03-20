package com.PremBhoot.unhingedgame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerForm extends LoginStates {
    private JFrame register;
    private JPanel pan;
    private JTextField userTextField, forenameTextField, emailTextField;
    private JPasswordField passwordTextField;
    private JButton button1, button2;
    private JLabel usernameLabel, forenameLabel, emailLabel, passwordLabel, postLabel;

    private Connection con;
    private PreparedStatement pst, pst2, pst3, pst4;
    private ResultSet rs;
    private LoginRegistrationManager lrm;

    private HashMap<Integer, String> character;

    public registerForm(LoginRegistrationManager lrm){
        populate();

        this.lrm = lrm;

        register = new JFrame("Unhinged Registration");
        register.setSize(400, 400);
        register.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        register.setVisible(true);
        register.setResizable(false);


        pan = new JPanel();
        pan.setLayout(null);
        register.add(pan);

        usernameLabel = new JLabel("Username");
        usernameLabel.setBounds(10, 20, 80, 25);
        usernameLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(usernameLabel);

        userTextField = new JTextField();
        userTextField.setBounds(100, 20, 205, 25);
        userTextField.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(userTextField);



        forenameLabel = new JLabel("Forename");
        forenameLabel.setBounds(10, 60, 75, 25);
        forenameLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(forenameLabel);

        forenameTextField = new JTextField();
        forenameTextField.setBounds(100, 60, 205, 25);
        forenameTextField.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(forenameTextField);

        emailLabel = new JLabel("Email");
        emailLabel.setBounds(10, 100, 80, 25);
        emailLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(emailLabel);

        emailTextField = new JTextField();
        emailTextField.setBounds(100, 100, 205, 25);
        emailTextField.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(emailTextField);


        passwordLabel = new JLabel("Password");
        passwordLabel.setBounds(10, 140, 80, 25);
        passwordLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(passwordLabel);

        passwordTextField = new JPasswordField();
        passwordTextField.setBounds(100, 140, 205, 25);
        passwordTextField.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(passwordTextField);

        postLabel = new JLabel("");
        postLabel.setBounds(10, 162, 350, 25);
        postLabel.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(postLabel);

        button1 = new JButton("Register");
        button1.setBounds(120, 190, 110, 25);
        button1.setFont(new Font("Verdana", Font.PLAIN, 12));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
                Matcher mat = pattern.matcher(emailTextField.getText());



                try{
                    boolean b = false;

                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/loginsystem", "root", "");
                    String query1 = "Select username from loginsystem where username=?";
                    pst = con.prepareStatement(query1);
                    pst.setString(1, userTextField.getText());
                    rs = pst.executeQuery();

                    if(rs.next()) b= true;

                    if(userTextField.getText().length() > 3 && forenameTextField.getText().length()>2
                       && emailTextField.getText().length()>4 && passwordTextField.getText().length()>3
                        && mat.matches() &&!b && userTextField.getText().length()<50 && forenameTextField.getText().length()<50
                         && emailTextField.getText().length()<128 && passwordTextField.getText().length()<(int)Math.pow(2, 128) ) {

                        String query = "insert into loginsystem (username, forename, email, password, userID) values (?, ?, ?, ?, ?)";

                        pst = con.prepareStatement(query);
                        pst.setString(1, userTextField.getText());
                        pst.setString(2, forenameTextField.getText());
                        pst.setString(3, emailTextField.getText());

                        Hash hash = new Hash();
                        String hashed = hash.generateHash(passwordTextField.getText());
                        pst.setString(4, hashed);

                        String str = "";
                        //str is userID after completing for loop
                        Random random = new Random();
                        for (int i = 0; i < 512; i++) {
                            int rand = random.nextInt(45) + 1;
                            str = str + character.get(rand);
                        }
                        pst.setString(5, str);
                        pst.execute();
                        userTextField.setText("");
                        forenameTextField.setText("");
                        emailTextField.setText("");
                        passwordTextField.setText("");
                        postLabel.setText("Registration Successful, return to login.");

                        String query2 = "insert into settings (userID, volumeMusic, volume) values (?, ?, ?)";
                        pst2=con.prepareStatement(query2);
                        pst2.setString(1, str);
                        pst2.setFloat(2, (float) 0.5);
                        pst2.setFloat(3, (float) 0.5);
                        pst2.execute();

                        String query3= "insert into leveluser (levelid, userID, time) values (?, ?, ?)";
                        pst3 = con.prepareStatement(query3);
                        pst3.setInt(1, 1);
                        pst3.setString(2, str);
                        pst3.setInt(3, 999999);
                        pst3.execute();





                    } else {
                        int total = 0;
                        if(userTextField.getText().length()<4 || userTextField.getText().length()>50){
                            postLabel.setText("Username Length Invalid (4 to 50 characters)");
                            total++;
                        }
                        if(forenameTextField.getText().length()<3 || forenameTextField.getText().length()>50){
                            postLabel.setText("Forename length invalid (3 or more characters)");
                            total++;
                        }
                        if(passwordTextField.getText().length()<4 || passwordTextField.getText().length()>(int)Math.pow(2, 128)){
                            postLabel.setText("Password Length invalid (4 or more characters)");
                            total++;
                        }
                        if(!mat.matches() || emailTextField.getText().length()>128){
                            postLabel.setText("Email invalid.");
                            total++;
                        }
                        if(b){
                            postLabel.setText("Username already exists - chose another");
                            total++;
                        }
                        if(total>1){
                            postLabel.setText(total + " fields are invalid");
                        }

                    }
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
        pan.add(button1);

        button2 = new JButton("Click to login");
        button2.setBounds(120, 240, 150, 25);
        button2.setFont(new Font("Verdana", Font.PLAIN, 12));
        pan.add(button2);
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginButton(e);
            }
        });

        pan.updateUI();
    }
    public void dispose(){
        register.dispose();
    }
    public void loginButton(ActionEvent event){
        lrm.registrationToLogin();
    }
    private void populate(){
        character = new HashMap<Integer, String>();
        character.put(1, "1");
        character.put(2, "2");
        character.put(3, "3");
        character.put(4, "4");
        character.put(5, "5");
        character.put(6, "6");
        character.put(7, "7");
        character.put(8, "8");
        character.put(9, "9");
        character.put(10, "a");
        character.put(11, "b");
        character.put(12, "c");
        character.put(13, "d");
        character.put(14, "e");
        character.put(15, "f");
        character.put(16, "g");
        character.put(17, "h");
        character.put(18, "i");
        character.put(19, "j");
        character.put(20, "k");
        character.put(21, "l");
        character.put(22, "m");
        character.put(23, "n");
        character.put(24, "o");
        character.put(25, "p");
        character.put(26, "q");
        character.put(27, "r");
        character.put(28, "s");
        character.put(29, "t");
        character.put(30, "u");
        character.put(31, "v");
        character.put(32, "w");
        character.put(33, "x");
        character.put(34, "y");
        character.put(35, "z");
        character.put(36, "!");
        character.put(37, "?");
        character.put(38, "$");
        character.put(39, "#");
        character.put(40, "%");
        character.put(41, ".");
        character.put(42, ",");
        character.put(43, "£");
        character.put(44, "*");
        character.put(45, "(");
        character.put(46, ")");


    }
    public boolean getLoggedIn(){return false;}

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public String getUserID() {
        return null;
    }
}
