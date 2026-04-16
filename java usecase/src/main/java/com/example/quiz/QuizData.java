package com.example.quiz;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class QuizData {
    private static String currentMockFile = "questions.txt";

    public static void setCurrentMock(String mockName) {
        currentMockFile = "mock_" + mockName + ".txt";
    }

    public static List<Question> loadQuestions() {
        List<Question> questions = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(currentMockFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String questionText = parts[0];
                    String[] options = parts[1].split(",");
                    int correct = Integer.parseInt(parts[2]);
                    questions.add(new Question(questionText, options, correct));
                }
            }
        } catch (IOException e) {
            // file not found or error, return empty
        }
        return questions;
    }

    public static void saveQuestions(List<Question> questions) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentMockFile))) {
            for (Question q : questions) {
                writer.write(q.getQuestionText() + ";");
                for (int i = 0; i < q.getOptions().length; i++) {
                    writer.write(q.getOptions()[i]);
                    if (i < q.getOptions().length - 1) writer.write(",");
                }
                writer.write(";" + q.getCorrectAnswerIndex());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}