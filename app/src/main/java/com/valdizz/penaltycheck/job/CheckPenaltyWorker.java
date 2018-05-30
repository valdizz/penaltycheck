package com.valdizz.penaltycheck.job;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.util.Log;

import com.valdizz.penaltycheck.PenaltyCheckApplication;
import com.valdizz.penaltycheck.model.NetworkService;
import com.valdizz.penaltycheck.model.NetworkServiceListener;
import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.util.CheckPermissionsUtils;
import com.valdizz.penaltycheck.util.NotificationUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.Worker;
import io.reactivex.disposables.Disposable;

import static com.valdizz.penaltycheck.PenaltyCheckApplication.TAG;

public class CheckPenaltyWorker extends Worker implements NetworkServiceListener {

    private Disposable disposable;
    @Inject
    NetworkService networkService;

    public CheckPenaltyWorker() {
        Log.d(TAG, "constructor!");
        PenaltyCheckApplication.getComponent().injectCheckPenaltyWorker(this);
    }

    @NonNull
    @Override
    public WorkerResult doWork() {
        Log.d(TAG, "doWork!");
        if (CheckPermissionsUtils.isOnline(getApplicationContext())){
            Log.d(TAG, "doWork!!!!!");
            RealmService realmService = new RealmService();
            try {
                List<Auto> autos = realmService.getAutos(true);
                disposable = networkService.checkPenalty(this, autos);
            } catch (Exception e) {
                Log.d(TAG, "FAILURE:" + e.getLocalizedMessage());
                return WorkerResult.FAILURE;
            }
            finally {
                realmService.closeRealm();
            }
            Log.d(TAG, "SUCCESS!");
            return WorkerResult.SUCCESS;
        }
        else {
            Log.d(TAG, "RETRY!");
            return WorkerResult.RETRY;
        }
    }

    @Override
    public void onSuccessRequest(long count) {
        Log.d(TAG, "onSuccessRequest!");
        if (count > 0){
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            Notification notification = notificationUtils.getNotification(count);
            notificationUtils.getManager().notify(NotificationUtils.NOTIFY_ID, notification);
        }
        disposable.dispose();
    }

    @Override
    public void onErrorRequest(String error) {
        Log.d(TAG, "onErrorRequest!");
        disposable.dispose();
    }

    public static PeriodicWorkRequest getCheckPenaltyWork() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        PeriodicWorkRequest.Builder checkPenaltyWorkerBuilder =
                new PeriodicWorkRequest.Builder(CheckPenaltyWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(TAG);
        Log.d(TAG, "getCheckPenaltyWork!");
        return checkPenaltyWorkerBuilder.build();
    }
}
