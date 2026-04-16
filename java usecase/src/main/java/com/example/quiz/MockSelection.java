package com.example.quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MockSelection extends JFrame {
    private JList<String> mockList;
    private DefaultListModel<String> listModel;

    public MockSelection() {
        setTitle("Select Mock Test");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel label = new JLabel("Available Mocks:");
        add(label, BorderLayout.NORTH);

        listModel = new DefaultListModel<>();
        loadAvailableMocks();

        mockList = new JList<>(listModel);
        mockList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(mockList);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton startButton = new JButton("Start Test");
        startButton.addActionListener(e -> startSelectedMock());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            dispose();
            new Launcher();
        });

        buttonPanel.add(startButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void loadAvailableMocks() {
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("mock_") && name.endsWith(".txt"));
        if (files != null && files.length > 0) {
            for (File file : files) {
                String mockName = file.getName().replace("mock_", "").replace(".txt", "");
                listModel.addElement(mockName);
            }
        } else {
            listModel.addElement("No mocks available");
        }
    }

    private void startSelectedMock() {
        int selectedIndex = mockList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "Please select a mock test.");
            return;
        }
        String selectedMock = listModel.getElementAt(selectedIndex);
        if (selectedMock.equals("No mocks available")) {
            JOptionPane.showMessageDialog(this, "No mock tests available. Please contact admin.");
            return;
        }
        dispose();
        new Main(selectedMock);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MockSelection());
    }
}
