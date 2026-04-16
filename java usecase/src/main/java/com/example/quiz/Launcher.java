package com.example.quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Launcher extends JFrame {
    public Launcher() {
        setTitle("Online Quiz System");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        JButton adminButton = new JButton("Admin");
        adminButton.addActionListener(e -> {
            dispose();
            new AdminMain();
        });

        JButton userButton = new JButton("User");
        userButton.addActionListener(e -> {
            dispose();
            new MockSelection();
        });

        add(new JLabel("Select Role:"));
        add(adminButton);
        add(userButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Launcher());
    }
}