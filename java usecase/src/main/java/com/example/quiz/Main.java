package com.example.quiz;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Enumeration;
import java.util.List;

public class Main extends JFrame {
    private List<Question> questions;
    private Quiz quiz;
    private ButtonGroup group;
    private JButton nextButton;
    private JLabel timerLabel;
    private Timer swingTimer;
    private int timeLeft;
    private String mockName;

    public Main() {
        this("default");
    }

    public Main(String mockName) {
        this.mockName = mockName;
        QuizData.setCurrentMock(mockName);
        questions = QuizData.loadQuestions();
        if (questions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No questions available in this mock. Please contact admin.");
            dispose();
            new MockSelection();
            return;
        }
        quiz = new Quiz(questions, 300); // 5 minutes
        quiz.startQuiz();
        timeLeft = 300;

        setTitle("Online Quiz - " + mockName);
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        timerLabel = new JLabel("Time left: 5:00", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(timerLabel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        add(centerPanel, BorderLayout.CENTER);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        nextButton = new JButton("Submit Answer");
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                submitAnswer();
            }
        });
        southPanel.add(nextButton);

        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quiz.stopQuiz();
                swingTimer.stop();
                dispose();
                new MockSelection();
            }
        });
        southPanel.add(backButton);

        add(southPanel, BorderLayout.SOUTH);

        swingTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeLeft--;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                timerLabel.setText("Time left: " + minutes + ":" + String.format("%02d", seconds));
                if (timeLeft <= 0) {
                    swingTimer.stop();
                    showResults();
                }
            }
        });
        swingTimer.start();

        showNextQuestion(centerPanel);

        setVisible(true);
    }

    private void showNextQuestion(JPanel centerPanel) {
        centerPanel.removeAll();
        Question q = quiz.getCurrentQuestion();
        if (q == null) {
            showResults();
            return;
        }

        JLabel questionLabel = new JLabel("<html><b>" + q.getQuestionText() + "</b></html>");
        questionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.add(questionLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.Y_AXIS));
        optionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        group = new ButtonGroup();
        for (int i = 0; i < q.getOptions().length; i++) {
            JRadioButton option = new JRadioButton(q.getOptions()[i]);
            option.setAlignmentX(Component.LEFT_ALIGNMENT);
            group.add(option);
            optionsPanel.add(option);
            optionsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        centerPanel.add(optionsPanel);

        revalidate();
        repaint();
    }

    private void submitAnswer() {
        int selected = -1;
        int i = 0;
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements(); ) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                selected = i;
                break;
            }
            i++;
        }
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Please select an answer.");
            return;
        }
        boolean hasNext = quiz.answerQuestion(selected);
        if (hasNext) {
            showNextQuestion((JPanel) getContentPane().getComponent(1));
        } else {
            showResults();
        }
    }

    private void showResults() {
        quiz.stopQuiz();
        swingTimer.stop();
        int score = quiz.getScore();
        int total = quiz.getTotalQuestions();
        double percentage = (double) score / total * 100;
        String performance = percentage >= 80 ? "Excellent" : percentage >= 60 ? "Good" : "Needs Improvement";
        JOptionPane.showMessageDialog(this, "Quiz completed!\n\nScore: " + score + "/" + total +
                "\nPercentage: " + String.format("%.2f", percentage) + "%" +
                "\nPerformance: " + performance, "Results", JOptionPane.INFORMATION_MESSAGE);
        dispose();
        new MockSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Main());
    }
}