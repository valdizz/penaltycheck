package com.valdizz.penaltycheck;

import android.app.Application;
import android.util.Log;

import com.valdizz.penaltycheck.dagger.AppComponent;
import com.valdizz.penaltycheck.dagger.DaggerAppComponent;
import com.valdizz.penaltycheck.job.CheckPenaltyWorker;

import java.io.IOException;

import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.WorkManager;
import io.reactivex.exceptions.UndeliverableException;
import io.reactivex.plugins.RxJavaPlugins;
import io.realm.Realm;

public class PenaltyCheckApplication extends Application {

    public static final String TAG = "PENALTY_CHECK_BLR";
    private static AppComponent component;

    @Override
    public void onCreate() {
        super.onCreate();
        //Stetho.initializeWithDefaults(this);
        Realm.init(this);
        component = DaggerAppComponent.create();
        initWorkManager();
        initRxErrorHandler();
    }

    public static AppComponent getComponent() {
        return component;
    }

    private void initWorkManager(){
        WorkManager.getInstance().enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.KEEP, CheckPenaltyWorker.getCheckPenaltyWork());
        Log.d(TAG, "WorkerManager starts: " + WorkManager.getInstance().getStatusesByTag(TAG));
    }

    private void initRxErrorHandler(){
        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException) {
                e = e.getCause();
                Log.e(PenaltyCheckApplication.TAG, "Undeliverable exception received", e);
            }
            if (e instanceof IOException) {
                // fine, irrelevant network problem or API that throws on cancellation
                return;
            }
            if (e instanceof InterruptedException) {
                // fine, some blocking code was interrupted by a dispose call
                return;
            }
            if ((e instanceof NullPointerException) || (e instanceof IllegalArgumentException)) {
                // that's likely a bug in the application
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            if (e instanceof IllegalStateException) {
                // that's a bug in RxJava or in a custom operator
                Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), e);
                return;
            }
            Log.e(PenaltyCheckApplication.TAG, "Undeliverable exception received, not sure what to do", e);
        });
    }
}
