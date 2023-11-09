package com.sprtcoding.obslearn.Model;

public class ScoreModel {
    String ID;
    int TOTAL_SCORE;

    public ScoreModel() {
    }

    public ScoreModel(int TOTAL_SCORE, String ID) {
        this.TOTAL_SCORE = TOTAL_SCORE;
        this.ID = ID;
    }

    public int getTOTAL_SCORE() {
        return TOTAL_SCORE;
    }

    public void setTOTAL_SCORE(int TOTAL_SCORE) {
        this.TOTAL_SCORE = TOTAL_SCORE;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
