package com.example.quiz;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Quiz {
    private List<Question> questions;
    private int timeLimit; // in seconds
    private int currentQuestionIndex;
    private int score;
    private Timer timer;
    private boolean timeUp;

    public Quiz(List<Question> questions, int timeLimit) {
        this.questions = questions;
        this.timeLimit = timeLimit;
        this.currentQuestionIndex = 0;
        this.score = 0;
        this.timeUp = false;
    }

    public void startQuiz() {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeUp = true;
                System.out.println("Time's up!");
            }
        }, timeLimit * 1000);
    }

    public Question getCurrentQuestion() {
        if (currentQuestionIndex < questions.size()) {
            return questions.get(currentQuestionIndex);
        }
        return null;
    }

    public boolean answerQuestion(int answerIndex) {
        if (timeUp) return false;
        Question q = questions.get(currentQuestionIndex);
        if (answerIndex == q.getCorrectAnswerIndex()) {
            score++;
        }
        currentQuestionIndex++;
        return currentQuestionIndex < questions.size();
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return questions.size();
    }

    public void stopQuiz() {
        if (timer != null) {
            timer.cancel();
        }
    }
}