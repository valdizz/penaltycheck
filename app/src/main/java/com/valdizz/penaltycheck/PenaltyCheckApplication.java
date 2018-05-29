package com.valdizz.penaltycheck;

import android.app.Application;

import com.evernote.android.job.JobManager;
import com.valdizz.penaltycheck.dagger.AppComponent;
import com.valdizz.penaltycheck.dagger.DaggerAppComponent;
import com.valdizz.penaltycheck.job.CheckPenaltyDailyJob;
import com.valdizz.penaltycheck.job.CheckPenaltyJobCreator;

import io.realm.Realm;

public class PenaltyCheckApplication extends Application {

    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        component = DaggerAppComponent.create();
        JobManager.create(this).addJobCreator(new CheckPenaltyJobCreator());
        CheckPenaltyDailyJob.schedule();
    }

    public static AppComponent getComponent() {
        return component;
    }
}
