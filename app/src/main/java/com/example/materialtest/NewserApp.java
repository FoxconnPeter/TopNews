package com.example.materialtest;

import android.app.Application;

/**
 * Created by gaolonglong on 2016/12/14.
 */

public class NewserApp extends Application {

    public static NewserApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
