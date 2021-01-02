package com.ntikhoa.chillnmovie.model;

import com.google.firebase.firestore.Exclude;

public class UserAccount {
    public static final int MALE = 0;
    public static final int FEMALE = 1;
    public static final int OTHER = 2;

    public static final int ADMIN = 0;
    public static final int EDITOR = 1;
    public static final int USER = 2;
    public static final int GUEST = 3;

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

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
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
