package com.group.NBAGManager.components;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String label) {
        super(label);

        // Make the button transparent
        setContentAreaFilled(false);
        setFocusPainted(false);

        // Set the text color to blue
        setForeground(Color.BLUE);

        // Set the font if necessary
        setFont(new Font("Arial", Font.BOLD, 12));
    }

    @Override
    protected void paintComponent(Graphics g) {
        // If the button is pressed, change the color to blue
        if (getModel().isPressed()) {
            g.setColor(Color.GRAY);
        } else {
            // Otherwise, the color is white
            g.setColor(Color.WHITE);
        }

        // Draw a rounded rectangle in the background of the button
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Call the superclass's method to do the actual drawing
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Draw the border of the button
        g.setColor(Color.BLUE);
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
    }
}