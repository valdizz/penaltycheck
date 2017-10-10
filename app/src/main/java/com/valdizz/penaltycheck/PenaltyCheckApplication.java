package com.valdizz.penaltycheck;

import android.app.Application;

import io.realm.Realm;

public class PenaltyCheckApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
