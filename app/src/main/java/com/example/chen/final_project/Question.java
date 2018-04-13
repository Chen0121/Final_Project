package com.example.chen.final_project;

public abstract class Question {
    private String question;
    private int type;

    String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int type() {
        return type;
    }
}

