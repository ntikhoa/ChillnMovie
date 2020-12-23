package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.Exclude;

public class UserAccount {
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public static final int ADMIN = 0;
    public static final int EDITOR = 1;
    public static final int USER = 2;

    private String email;
    private String name;
    private String birthdate;
    private String country;
    private String avatarPath;
    private String startDate;
    private int gender;
    private int typeAccount;

    public UserAccount() {
    }

    public UserAccount(String email, String startDate, int mode) {
        this.email = email;
        this.typeAccount = mode;
        this.startDate = startDate;
    }

    public String getName() {
        return name;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getCountry() {
        return country;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public int getGender() {
        return gender;
    }

    public int getTypeAccount() {
        return typeAccount;
    }

    public String getEmail() {
        return email;
    }

    public String getStartDate() {
        return startDate;
    }
}
