package com.sprtcoding.obslearn.Model;

public class QuestionModel {
    String Question, optionA, optionB, optionC, optionD, question_ID;
    int CorrectAnswer, selectedAnswer, status;

    public QuestionModel() {
    }

    public QuestionModel(String question, String optionA, String optionB, String optionC, String optionD, String question_ID, int correctAnswer, int selectedAnswer, int status) {
        Question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.question_ID = question_ID;
        CorrectAnswer = correctAnswer;
        this.selectedAnswer = selectedAnswer;
        this.status = status;
    }

    public String getQuestion_ID() {
        return question_ID;
    }

    public void setQuestion_ID(String question_ID) {
        this.question_ID = question_ID;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(int correctAnswer) {
        CorrectAnswer = correctAnswer;
    }
}
