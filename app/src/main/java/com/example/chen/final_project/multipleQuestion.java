package com.example.chen.final_project;

public class multipleQuestion extends Question {
    private String answerA;
    private String answerB;
    private String answerC;
    private String answerD;
    private String correct;


    public multipleQuestion(){
        type="multipleChoice";
    }

    public multipleQuestion(String answerA, String answerB, String answerC, String answerD, String question, String correct){
        setAnswerA(answerA);
        setAnswerB(answerB);
        setAnswerC(answerC);
        setAnswerD(answerD);
        setQuestion(question);
        setCorrect(correct);
        type="multipleChoice";
    }

    public String getAnswerA(){
        return answerA;
    }

    public String getAnswerB() {
        return answerB;
    }

    public String getAnswerC() {
        return answerC;
    }

    public String getAnswerD() {
        return answerD;
    }

    public String getCorrect(){
        return correct;
    }

    public void setAnswerA(String answerA){
        this.answerA=answerA;
    }

    public void setAnswerB(String answerB){
        this.answerB=answerB;
    }

    public void setAnswerC(String answerC){
        this.answerC=answerC;
    }

    public void setAnswerD(String answerD){
        this.answerD=answerD;
    }

    public void setCorrect(String correct){
        this.correct=correct;
    }

}
