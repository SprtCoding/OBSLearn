package com.sprtcoding.obslearn.Model;

public class CatListModel {
    String CAT_ID, NAME;
    int NO_OF_TEST;

    public CatListModel() {
    }

    public CatListModel(String CAT_ID, String NAME, int NO_OF_TEST) {
        this.CAT_ID = CAT_ID;
        this.NAME = NAME;
        this.NO_OF_TEST = NO_OF_TEST;
    }

    public String getCAT_ID() {
        return CAT_ID;
    }

    public void setCAT_ID(String CAT_ID) {
        this.CAT_ID = CAT_ID;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public int getNO_OF_TEST() {
        return NO_OF_TEST;
    }

    public void setNO_OF_TEST(int NO_OF_TEST) {
        this.NO_OF_TEST = NO_OF_TEST;
    }
}
