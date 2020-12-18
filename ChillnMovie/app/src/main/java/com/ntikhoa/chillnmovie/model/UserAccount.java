package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.Exclude;

public class UserAccount {
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    public static final int ADMIN = 0;
    public static final int EDITOR = 1;
    public static final int USER = 3;

    private String name;
    private String birthdate;
    private String country;
    private String userId;
    private String avatarPath;
    private int gender;
    private int typeAccount;

    private UserAccount() {
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

    @Exclude
    public String getUserId() {
        return userId;
    }
}
