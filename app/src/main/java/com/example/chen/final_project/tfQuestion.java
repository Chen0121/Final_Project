package com.example.chen.final_project;

public class tfQuestion  extends Question {

    private boolean answer;

    public tfQuestion() {

    }

    public tfQuestion(boolean answer,String question) {
        setAnswer(answer);
        setQuestion(question);
    }

    public boolean isRight() {
        return answer;
    }

    public void setAnswer(boolean answer) {
        this.answer = answer;
    }
}
