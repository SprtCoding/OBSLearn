package com.sprtcoding.obslearn.Model;

public class TestModel {
    String TEST_ID, TEST_TYPE, QUESTION, CORRECT_ANSWER, C1, C2, C3, C4, TIME, DATE, TOPICS;

    public TestModel() {
    }

    public TestModel(String UID, String testType, String question, String correctAnswer, String choicesOne, String choicesTwo, String choicesThree, String choicesFour, String time, String date, String topics) {
        this.TEST_ID = UID;
        this.TEST_TYPE = testType;
        this.QUESTION = question;
        this.CORRECT_ANSWER = correctAnswer;
        this.C1 = choicesOne;
        this.C2 = choicesTwo;
        this.C3 = choicesThree;
        this.C4 = choicesFour;
        this.TIME = time;
        this.DATE = date;
        this.TOPICS = topics;
    }

    public String getTOPICS() {
        return TOPICS;
    }

    public void setTOPICS(String TOPICS) {
        this.TOPICS = TOPICS;
    }

    public String getTEST_ID() {
        return TEST_ID;
    }

    public void setTEST_ID(String TEST_ID) {
        this.TEST_ID = TEST_ID;
    }

    public String getTEST_TYPE() {
        return TEST_TYPE;
    }

    public void setTEST_TYPE(String TEST_TYPE) {
        this.TEST_TYPE = TEST_TYPE;
    }

    public String getQUESTION() {
        return QUESTION;
    }

    public void setQUESTION(String QUESTION) {
        this.QUESTION = QUESTION;
    }

    public String getCORRECT_ANSWER() {
        return CORRECT_ANSWER;
    }

    public void setCORRECT_ANSWER(String CORRECT_ANSWER) {
        this.CORRECT_ANSWER = CORRECT_ANSWER;
    }

    public String getC1() {
        return C1;
    }

    public void setC1(String c1) {
        C1 = c1;
    }

    public String getC2() {
        return C2;
    }

    public void setC2(String c2) {
        C2 = c2;
    }

    public String getC3() {
        return C3;
    }

    public void setC3(String c3) {
        C3 = c3;
    }

    public String getC4() {
        return C4;
    }

    public void setC4(String c4) {
        C4 = c4;
    }

    public String getTIME() {
        return TIME;
    }

    public void setTIME(String TIME) {
        this.TIME = TIME;
    }

    public String getDATE() {
        return DATE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }
}
