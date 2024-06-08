package com.group.NBAGManager.model;
import javax.swing.*;
import java.awt.*;

//class to help create java swing elements such as buttons,labels and text fields
public class GuiCreator {

    //create button with specified text,font,foreground color and background colour
    public static JButton createButton(String text, Font font, Color foreground, Color background, boolean borderPainted, int x, int y) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setForeground(foreground);
        button.setBackground(background);
        button.setBorderPainted(borderPainted);
        return button;
    }

    // Create label with specified text, font, foreground color, and position
    public static JLabel createLabel(String text, Font font, Color foreground, int x, int y) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(foreground);
        //label.setBounds(x, y, label.getPreferredSize().width, label.getPreferredSize().height);
        return label;
    }

    // Create text field with specified columns, font, foreground color, background color, border, and position
    public static JTextField createTextField(int columns, Font font, Color foreground, Color background, boolean border, int x, int y) {
        JTextField textField = new JTextField(columns);
        textField.setFont(font);
        textField.setForeground(foreground);
        textField.setBackground(background);
        //if boolean border is false, there will be no border
        if (!border) {
            textField.setBorder(null);
        }
        // textField.setBounds(x, y, textField.getPreferredSize().width, textField.getPreferredSize().height);
        return textField;
    }

    // Create password field with specified columns, font, foreground color, background color, border, and position
    public static JPasswordField createPasswordField(int columns, Font font, Color foreground, Color background, boolean border, int x, int y) {
        JPasswordField passwordField = new JPasswordField(columns);
        passwordField.setFont(font);
        passwordField.setForeground(foreground);
        passwordField.setBackground(background);
        //if boolean border is false, there will be no border
        if (!border) {
            passwordField.setBorder(null);
        }
        //passwordField.setBounds(x, y, passwordField.getPreferredSize().width, passwordField.getPreferredSize().height);
        return passwordField;
    }
}