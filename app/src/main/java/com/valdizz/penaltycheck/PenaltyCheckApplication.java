package com.valdizz.penaltycheck;

import android.app.Application;
import android.util.Log;

import com.valdizz.penaltycheck.dagger.AppComponent;
import com.valdizz.penaltycheck.dagger.DaggerAppComponent;
import com.valdizz.penaltycheck.job.CheckPenaltyWorker;

import java.util.List;

import androidx.work.WorkManager;
import androidx.work.WorkStatus;
import io.realm.Realm;

public class PenaltyCheckApplication extends Application {

    public static final String TAG = "PENALTY_CHECK";
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        component = DaggerAppComponent.create();
        WorkManager.getInstance().cancelAllWorkByTag(TAG);
        Log.d(TAG, "WorkManager cancelAllWorkByTag!");
        WorkManager.getInstance().enqueue(CheckPenaltyWorker.getCheckPenaltyWork());
        Log.d(TAG, "WorkManager started: " + WorkManager.getInstance().getStatusesByTag(TAG));
    }

    public static AppComponent getComponent() {
        return component;
    }
}
