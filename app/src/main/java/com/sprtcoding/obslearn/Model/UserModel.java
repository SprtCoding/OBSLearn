package com.sprtcoding.obslearn.Model;

public class UserModel {
    String NAME, EMAIL_ID, USER_ID, GENDER, SECTION;

    public UserModel() {
    }

    public UserModel(String NAME, String EMAIL_ID, String USER_ID, String GENDER, String SECTION) {
        this.NAME = NAME;
        this.EMAIL_ID = EMAIL_ID;
        this.USER_ID = USER_ID;
        this.GENDER = GENDER;
        this.SECTION = SECTION;
    }

    public String getSECTION() {
        return SECTION;
    }

    public void setSECTION(String SECTION) {
        this.SECTION = SECTION;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getEMAIL_ID() {
        return EMAIL_ID;
    }

    public void setEMAIL_ID(String EMAIL_ID) {
        this.EMAIL_ID = EMAIL_ID;
    }

    public String getUSER_ID() {
        return USER_ID;
    }

    public void setUSER_ID(String USER_ID) {
        this.USER_ID = USER_ID;
    }
}
