package com.group.NBAGManager.LoginPage;

import com.group.NBAGManager.AddPlayer;
import com.group.NBAGManager.App;
import com.group.NBAGManager.model.*;
import com.group.NBAGManager.repository.PlayerRepository;
import com.group.NBAGManager.repository.UserRepository;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.List;


public class LoginMenu extends JFrame {

    UserRepository userRepository = RepositoryHandler.getInstance().getUserRepository();
    PlayerRepository playerRepository = RepositoryHandler.getInstance().getPlayerRepository();
    private JTextField usernameField;
    private JPasswordField passwordField;


    // Constructor for the LoginMenu class
    public LoginMenu() throws IOException {
        APIHandler apiHandler = new APIHandler();

        List<Player> currentPlayers = playerRepository.findAll();
        List<Player> players = apiHandler.getMarketPlayers();

        System.out.println("Number of players: " + players.size());

        // update database information for new players/changed stats of players
        players.forEach(player -> {
            int index = currentPlayers.indexOf(player);
            if (index != -1) {
                Player repoPlayer = currentPlayers.get(index);
                if (!repoPlayer.equals(player)) {
                    playerRepository.update(player);
                }
            }
            else {
                playerRepository.save(player);
            }
        });

        //set JFrame properties
        setTitle("NBA G-Manager");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setLocationRelativeTo(null);

        //set the backgroundImage
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

        //set the window size to be constant
        setResizable(false);


        //create and add title label
        //create titleLabel using GuiCreator class
        JLabel titleLabel = GuiCreator.createLabel("Welcome to NBA G-Manager!", new Font("Roboto", Font.BOLD, 20), Color.decode("#E2B714"), 0, 0);
        gridbag.gridx = 0;
        gridbag.gridy = 0;
        gridbag.gridwidth = 2;
        backgroundLabel.add(titleLabel,gridbag);

        //create and add subtitle label
        //create subTitleLabel using GuiCreator class
        JLabel subTitleLabel = GuiCreator.createLabel("by Group...", new Font("Roboto Mono", Font.PLAIN, 14), Color.decode("#E2B714"), 0, 1);
        gridbag.gridx = 0;
        gridbag.gridy = 1;
        gridbag.gridwidth = 2;
        backgroundLabel.add(subTitleLabel,gridbag);

        //create and add username label
        //create usernameLabel using GuiCreator class
        JLabel usernameLabel = GuiCreator.createLabel("Username", new Font("Roboto Mono", Font.PLAIN, 13), Color.decode("#E2B714"), 0, 2);
        gridbag.gridx = 0;
        gridbag.gridy = 2;
        gridbag.gridwidth = 1;
        backgroundLabel.add(usernameLabel,gridbag);

        //create and add password label
        //create passwordLabel using GuiCreator class
        JLabel passwordLabel = GuiCreator.createLabel("Password", new Font("Roboto Mono", Font.PLAIN, 13), Color.decode("#E2B714"), 0, 3);
        gridbag.gridx = 0;
        gridbag.gridy = 3;
        gridbag.gridwidth = 1;
        backgroundLabel.add(passwordLabel,gridbag);

        // Create and add username text field
        // create usernameField using GuiCreator class
        usernameField = GuiCreator.createTextField(8, new Font("Roboto Mono", Font.PLAIN, 13), Color.white, Color.decode("#2C2E31"), true, 1, 2);
        gridbag.gridx = 1;
        gridbag.gridy = 2;
        gridbag.gridwidth = 1;
        gridbag.fill = GridBagConstraints.HORIZONTAL;
        backgroundLabel.add(usernameField,gridbag);

        //create and add password text field
        //create passwordField using GuiCreator class
        passwordField = GuiCreator.createPasswordField(8, new Font("Roboto Mono", Font.PLAIN, 13), Color.white, Color.decode("#2C2E31"), true, 1, 3);
        gridbag.gridx = 1;
        gridbag.gridy = 3;
        gridbag.gridwidth = 1;
        gridbag.fill = GridBagConstraints.HORIZONTAL;
        backgroundLabel.add(passwordField,gridbag);

        //create and add login button
        //create button using GuiCreator class
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

        //add MouseListener so that the button glows when hovering over it
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

        // register method is called when register button is clicked
        registerButton.addActionListener((ActionEvent e) ->{
            register();
        });
    }

    //method to handle login logic
    private void login() {
        //get the username and password that the user entered in the textField
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);


        //check for empty fields
        if (username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        //retrieve user from repository
        User currentUser =  userRepository.findUserByUsername(username);
        if(currentUser == null){
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String hashedPassword = SecureEncryptor.hashPassword(password,currentUser.getSalt());

        //check if the password matches
        if (currentUser != null && currentUser.getPassword().equals(hashedPassword)) {
            //sets the loggedInUser as the currentUser
            CurrentSession.getInstance().setLoggedInUser(currentUser);
            //open the add player menu if is first login
            if(currentUser.isFirstLogin()){
                AddPlayer addPlayer = new AddPlayer();
                addPlayer.displayForm();
                dispose();
                //else open the main application
            }else {
                App app = new App();
                app.displayForm();
                dispose();
            }
        } else {
            //prompt message if password or username is incorrect
            JOptionPane.showMessageDialog(this, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to handle registration logic
    private void register(){
        //get the username and password that the user entered in the textField
        String username = usernameField.getText();
        char[] passwordChars= passwordField.getPassword();
        String password = new String(passwordChars);

        //check if the username is already taken
        if (usernameUsed(username)){
            JOptionPane.showMessageDialog(this, "Username has already been taken");
            return;
        }

        //validate username and password
        if (username.equals("")) {
            JOptionPane.showMessageDialog(this, "Username can't be empty", "Error", JOptionPane.ERROR_MESSAGE);
        } else if(password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter your password", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (password.length()<8) {
            //extra condition to make sure user enters a password that has a length of atleast 8 characters
            JOptionPane.showMessageDialog(this, "Password is weak. Passsword need to be at least 8 characters");
        } else {
            //hash the password using SecureEncryptor class for security when storing into database
            byte[] salt = SecureEncryptor.generateSalt();
            String hashedPassword = SecureEncryptor.hashPassword(password,salt);

            //create a new user object and add to the repository
            User newUser = new User(username,hashedPassword,salt,true);
            userRepository.save(newUser);
            JOptionPane.showMessageDialog(this, "Registration Successful!");
        }
    }

    //method to check if the username is already in use
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

