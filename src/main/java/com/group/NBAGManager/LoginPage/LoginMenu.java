package com.group.NBAGManager.LoginPage;
import com.group.NBAGManager.App;
import com.group.NBAGManager.PlayerPerformanceRanking;
import com.group.NBAGManager.model.CurrentSession;
import com.group.NBAGManager.model.RepositoryHandler;
import com.group.NBAGManager.model.User;
import com.group.NBAGManager.repository.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class LoginMenu extends JFrame {

    UserRepository userRepository = RepositoryHandler.getInstance().getUserRepository();
    private JTextField usernameField;
    private JPasswordField passwordField;


    // Constructor for the LoginMenu class
    public LoginMenu() throws IOException {
        // Initialize the user repository


        // Set JFrame properties
        setTitle("NBA G-Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);


        // Set background
        //JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\SEM 2\\DS2024\\DSASSIGNMENT\\loginMenuBackground.jpeg"));
        //add(backgroundLabel);
        //backgroundLabel.setLayout(new GridBagLayout());
        //setContentPane(backgroundLabel);
        ImageIcon backgroundImage = new ImageIcon("src/main/resources/images/login-background.jpeg");

        // Resize the image to fit the window size
        Image img = backgroundImage.getImage();
        Image resizedImg = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        backgroundImage = new ImageIcon(resizedImg);

        // Set the image as the content pane
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new GridBagLayout());
        setContentPane(backgroundLabel);

        GridBagConstraints gridbag = new GridBagConstraints();
        gridbag.insets = new Insets(15, 15, 15, 15);

        setResizable(true);
        //JLabel backgroundLabel = new JLabel(new ImageIcon("C:\\SEM 2\\DS2024\\DSASSIGNMENT\\loginMenuBackground.jpeg"));
        //add(backgroundLabel);
        //backgroundLabel.setLayout(new GridBagLayout());
        //setContentPane(backgroundLabel);

        ImageIcon finalBackgroundImage = backgroundImage;
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Resize the image to fit the new window size
                Image img = finalBackgroundImage.getImage();
                Image resizedImg = img.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                finalBackgroundImage.setImage(resizedImg);
                backgroundLabel.setIcon(finalBackgroundImage);
            }
        });



        // Create a panel to hold the components
        //JPanel panel = new JPanel(new GridBagLayout());
        //panel.setOpaque(false); // Make the panel transparent
        //getContentPane().add(panel);

        // Create and add title label
        JLabel titleLabel = GuiCreator.createLabel("Welcome to NBA G-Manager!", new Font("Roboto", Font.BOLD, 20), Color.decode("#E2B714"), 0, 0);
        gridbag.gridx = 0;
        gridbag.gridy = 0;
        gridbag.gridwidth = 2;
        backgroundLabel.add(titleLabel,gridbag);

        // Create and add subtitle label
        JLabel subTitleLabel = GuiCreator.createLabel("by Group...", new Font("Roboto Mono", Font.PLAIN, 14), Color.decode("#E2B714"), 0, 1);
        gridbag.gridx = 0;
        gridbag.gridy = 1;
        gridbag.gridwidth = 2;
        backgroundLabel.add(subTitleLabel,gridbag);

        // Create and add username label
        JLabel usernameLabel = GuiCreator.createLabel("Username", new Font("Roboto Mono", Font.PLAIN, 13), Color.decode("#E2B714"), 0, 2);
        gridbag.gridx = 0;
        gridbag.gridy = 2;
        gridbag.gridwidth = 1;
        backgroundLabel.add(usernameLabel,gridbag);

        // Create and add password label
        JLabel passwordLabel = GuiCreator.createLabel("Password", new Font("Roboto Mono", Font.PLAIN, 13), Color.decode("#E2B714"), 0, 3);
        gridbag.gridx = 0;
        gridbag.gridy = 3;
        gridbag.gridwidth = 1;
        backgroundLabel.add(passwordLabel,gridbag);

        // Create and add username text field
        usernameField = GuiCreator.createTextField(8, new Font("Roboto Mono", Font.PLAIN, 13), Color.white, Color.decode("#2C2E31"), true, 1, 2);
        gridbag.gridx = 1;
        gridbag.gridy = 2;
        gridbag.gridwidth = 1;
        gridbag.fill = GridBagConstraints.HORIZONTAL;
        backgroundLabel.add(usernameField,gridbag);

        // Create and add password text field
        passwordField = GuiCreator.createPasswordField(8, new Font("Roboto Mono", Font.PLAIN, 13), Color.white, Color.decode("#2C2E31"), true, 1, 3);
        gridbag.gridx = 1;
        gridbag.gridy = 3;
        gridbag.gridwidth = 1;
        gridbag.fill = GridBagConstraints.HORIZONTAL;
        backgroundLabel.add(passwordField,gridbag);

        // Create and add login button
        JButton loginButton = GuiCreator.createButton("Login", new Font("Roboto Mono", Font.BOLD, 15), Color.decode("#646669"), Color.decode("#323437"), false, 1, 4);
        gridbag.gridx = 1;
        gridbag.gridy = 4;
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                loginButton.setForeground(Color.white);
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                loginButton.setForeground(Color.decode("#646669"));
            }
        });
        backgroundLabel.add(loginButton, gridbag);
        loginButton.addActionListener((ActionEvent e) ->{
            login();
        });


        // Create and add register button
        JButton registerButton = GuiCreator.createButton("Register", new Font("Roboto Mono", Font.BOLD, 15), Color.decode("#646669"), Color.decode("#323437"), false, 0, 4);
        gridbag.gridx = 0;
        gridbag.gridy = 4;
        //add(registerButton,gridbag);
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent evt) {
                registerButton.setForeground(Color.white);
            }
            @Override
            public void mouseExited(MouseEvent evt) {
                registerButton.setForeground(Color.decode("#646669"));
            }
        });
        backgroundLabel.add(registerButton, gridbag);

        registerButton.addActionListener((ActionEvent e) ->{
            register();
        });



    }
    private void login() {
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        // Check for empty fields
        if (username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Retrieve user from repository
        User currentUser =  userRepository.findUserByUsername(username);
        //String hashedPassword = SecureEncryptor.hashPassword(password,currentUser.getSalt());

        // Check if the password matches
        if (currentUser != null && currentUser.getPassword().equals(password)) {
            // Open the main menu
            CurrentSession.getInstance().setLoggedInUser(currentUser);

            //userRepository.setCurrentUser(currentUser);
            JOptionPane.showMessageDialog(this, "Login successful!");
            App app = new App();
            app.displayForm();
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle registration logic
    private void register(){
        String username = usernameField.getText();
        char[] passwordChars= passwordField.getPassword();
        String password = new String(passwordChars);

        // Check if the username is already taken
        if (usernameUsed(username)){
            JOptionPane.showMessageDialog(this, "Username has already been taken");
            return;
        }

        // Validate username and password
        if (username.equals("")) {
            JOptionPane.showMessageDialog(this, "Username can't be empty", "Error", JOptionPane.ERROR_MESSAGE);
        } else if(password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter your password", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            byte[] salt = SecureEncryptor.generateSalt();
            String hashedPassword = SecureEncryptor.hashPassword(password,salt);

            // Create a new user and add to the repository
            User newUser = new User(username,hashedPassword,"salt");
            userRepository.save(newUser);
            JOptionPane.showMessageDialog(this, "Registration Successful!");
        }
    }

    // Method to check if the username is already in use
    private boolean usernameUsed(String username){
        User user = userRepository.findUserByUsername(username);
        return user != null;
    }
    public static void main(String[] args) {
        //initiate all repositories and create hook to close all repositories when the application is closed
        try{
            RepositoryHandler.getInstance();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                RepositoryHandler.getInstance().closeAll();
            }));
        }catch (RuntimeException e){
            JOptionPane.showMessageDialog(null, "Error while communicating with the database.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginMenu().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}

