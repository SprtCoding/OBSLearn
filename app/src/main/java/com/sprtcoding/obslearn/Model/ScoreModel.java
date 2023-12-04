package com.sprtcoding.obslearn.Model;

public class ScoreModel {
    String ID;
    String scoreNum;
    int TOTAL_SCORE;

    public ScoreModel() {
    }

    public ScoreModel(String ID, String scoreNum, int TOTAL_SCORE) {
        this.ID = ID;
        this.scoreNum = scoreNum;
        this.TOTAL_SCORE = TOTAL_SCORE;
    }

    public String getScoreNum() {
        return scoreNum;
    }

    public void setScoreNum(String scoreNum) {
        this.scoreNum = scoreNum;
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
