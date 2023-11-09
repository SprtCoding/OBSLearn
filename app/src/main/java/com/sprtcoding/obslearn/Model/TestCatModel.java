package com.sprtcoding.obslearn.Model;

public class TestCatModel {
    String testID, fieldNameID, fieldNameTime;
    int topScore, time;

    public TestCatModel() {
    }

    public TestCatModel(String testID, String fieldNameID, String fieldNameTime, int topScore, int time) {
        this.testID = testID;
        this.fieldNameID = fieldNameID;
        this.fieldNameTime = fieldNameTime;
        this.topScore = topScore;
        this.time = time;
    }

    public String getFieldNameID() {
        return fieldNameID;
    }

    public void setFieldNameID(String fieldNameID) {
        this.fieldNameID = fieldNameID;
    }

    public String getFieldNameTime() {
        return fieldNameTime;
    }

    public void setFieldNameTime(String fieldNameTime) {
        this.fieldNameTime = fieldNameTime;
    }

    public String getTestID() {
        return testID;
    }

    public void setTestID(String testID) {
        this.testID = testID;
    }

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
