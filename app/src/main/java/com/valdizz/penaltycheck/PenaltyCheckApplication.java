package com.valdizz.penaltycheck;

import android.app.Application;
import android.util.Log;

import com.valdizz.penaltycheck.dagger.AppComponent;
import com.valdizz.penaltycheck.dagger.DaggerAppComponent;
import com.valdizz.penaltycheck.job.CheckPenaltyWorker;


import androidx.work.WorkManager;
import io.realm.Realm;

public class PenaltyCheckApplication extends Application {

    public static final String TAG = "PENALTY_CHECK_BLR";
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        component = DaggerAppComponent.create();
        WorkManager.getInstance().cancelAllWorkByTag(TAG);
        Log.d(TAG, "WorkerManager cancels all works!");
        WorkManager.getInstance().enqueue(CheckPenaltyWorker.getCheckPenaltyWork());
        Log.d(TAG, "WorkerManager starts: " + WorkManager.getInstance().getStatusesByTag(TAG));
    }

    public static AppComponent getComponent() {
        return component;
    }
}
