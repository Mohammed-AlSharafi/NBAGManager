package com.group.NBAGManager.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RoundedButton extends JButton {

    public RoundedButton(String label) {
        super(label);

        // making the button transparent
        setContentAreaFilled(false);
        setFocusPainted(false);

        // setting the text color
        setForeground(Color.DARK_GRAY);

        // setting the font
        setFont(new Font("Arial", Font.BOLD, 12));

    }

    @Override
    protected void paintComponent(Graphics g) {
        // changing the color during pressing to gray
        if (getModel().isPressed()) {
            g.setColor(Color.GRAY);
        } else {
            // else setting the color to translucent gray
            g.setColor(new Color(128, 128, 128, 64));
        }

        // Draw a rounded rectangle in the background of the button
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        // Call the superclass's method to do the actual drawing
        super.paintComponent(g);
    }

    @Override
    protected void paintBorder(Graphics g) {
        // Draw the border of the button
        g.setColor(Color.GRAY);
        g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 30, 30);
    }
}