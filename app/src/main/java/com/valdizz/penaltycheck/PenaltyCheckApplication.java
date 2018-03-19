package com.valdizz.penaltycheck;

import android.app.Application;

import com.valdizz.penaltycheck.dagger.AppComponent;
import com.valdizz.penaltycheck.dagger.DaggerAppComponent;

import io.realm.Realm;

public class PenaltyCheckApplication extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        component = DaggerAppComponent.create();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
