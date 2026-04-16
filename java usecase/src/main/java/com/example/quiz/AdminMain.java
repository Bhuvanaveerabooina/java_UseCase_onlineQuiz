package com.example.quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

public class AdminMain extends JFrame {
    private String selectedMock = "default";
    private JLabel mockLabel;

    public AdminMain() {
        setTitle("Admin Panel");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Current Mock: "));
        mockLabel = new JLabel(selectedMock);
        mockLabel.setFont(new Font("Arial", Font.BOLD, 12));
        topPanel.add(mockLabel);

        JButton selectMockButton = new JButton("Select/Create Mock");
        selectMockButton.addActionListener(e -> selectMock());
        topPanel.add(selectMockButton);

        add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new FlowLayout());
        centerPanel.add(new JLabel("Questions for: " + selectedMock));

        JButton addQuestionButton = new JButton("Add Question");
        addQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addQuestion();
            }
        });

        JButton viewQuestionsButton = new JButton("View Questions");
        viewQuestionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewQuestions();
            }
        });

        centerPanel.add(addQuestionButton);
        centerPanel.add(viewQuestionsButton);

        add(centerPanel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout());

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new Launcher();
            }
        });
        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void selectMock() {
        String[] options = {"Create New Mock", "Select Existing Mock", "Cancel"};
        int choice = JOptionPane.showOptionDialog(this, "What would you like to do?", "Mock Selection",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choice == 0) {
            String mockName = JOptionPane.showInputDialog(this, "Enter new mock name:");
            if (mockName != null && !mockName.trim().isEmpty()) {
                selectedMock = mockName.trim();
                mockLabel.setText(selectedMock);
                QuizData.setCurrentMock(selectedMock);
                JOptionPane.showMessageDialog(this, "Mock '" + selectedMock + "' selected.");
            }
        } else if (choice == 1) {
            java.util.List<String> mocksList = getAvailableMocks();
            if (mocksList.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No existing mocks found.");
                return;
            }
            String[] mocks = mocksList.toArray(new String[0]);
            String selected = (String) JOptionPane.showInputDialog(this, "Select a mock:", "Available Mocks",
                    JOptionPane.QUESTION_MESSAGE, null, mocks, mocks[0]);
            if (selected != null) {
                selectedMock = selected;
                mockLabel.setText(selectedMock);
                QuizData.setCurrentMock(selectedMock);
                JOptionPane.showMessageDialog(this, "Mock '" + selectedMock + "' selected.");
            }
        }
    }

    private java.util.List<String> getAvailableMocks() {
        java.util.List<String> mocks = new java.util.ArrayList<>();
        File dir = new File(".");
        File[] files = dir.listFiles((d, name) -> name.startsWith("mock_") && name.endsWith(".txt"));
        if (files != null) {
            for (File file : files) {
                String mockName = file.getName().replace("mock_", "").replace(".txt", "");
                mocks.add(mockName);
            }
        }
        return mocks;
    }

    private void addQuestion() {
        JTextField questionField = new JTextField(20);
        JTextField optionsField = new JTextField(20);
        JTextField correctField = new JTextField(5);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Question:"));
        panel.add(questionField);
        panel.add(new JLabel("Options (comma separated):"));
        panel.add(optionsField);
        panel.add(new JLabel("Correct answer index (0-based):"));
        panel.add(correctField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Question", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String questionText = questionField.getText();
            String[] options = optionsField.getText().split(",");
            for (int i = 0; i < options.length; i++) {
                options[i] = options[i].trim();
            }
            int correct = Integer.parseInt(correctField.getText().trim());
            Question q = new Question(questionText, options, correct);
            List<Question> questions = QuizData.loadQuestions();
            questions.add(q);
            QuizData.saveQuestions(questions);
            JOptionPane.showMessageDialog(this, "Question added to '" + selectedMock + "'!");
        }
    }

    private void viewQuestions() {
        List<Question> questions = QuizData.loadQuestions();
        StringBuilder sb = new StringBuilder();
        if (questions.isEmpty()) {
            sb.append("No questions in this mock.");
        } else {
            for (int i = 0; i < questions.size(); i++) {
                Question q = questions.get(i);
                sb.append("Q").append(i+1).append(": ").append(q.getQuestionText()).append("\n");
                sb.append("Options: ");
                for (String opt : q.getOptions()) sb.append(opt).append(", ");
                sb.append("\nCorrect: ").append(q.getCorrectAnswerIndex()).append("\n\n");
            }
        }
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        JOptionPane.showMessageDialog(this, scrollPane, "Questions in '" + selectedMock + "'", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminMain());
    }
}