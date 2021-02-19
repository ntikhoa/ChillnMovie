package com.ntikhoa.chillnmovie.model;

import android.app.Application;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class UserModeSingleton extends Application {
    private static UserModeSingleton instance;

    private int mode;

    public static UserModeSingleton getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mode = UserAccount.GUEST;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}
