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



public class CheckPenaltyWorker extends Worker implements NetworkServiceListener {

    @Inject NetworkService networkService;

    public CheckPenaltyWorker() {
        Log.d(PenaltyCheckApplication.TAG, "Worker constructor!");
        PenaltyCheckApplication.getComponent().injectCheckPenaltyWorker(this);
    }

    @NonNull
    @Override
    public Worker.Result doWork() {
        Log.d(PenaltyCheckApplication.TAG, "Worker starts!");
        if (CheckPermissionsUtils.isOnline(getApplicationContext())){
            RealmService realmService = new RealmService();
            try {
                List<Auto> autos = realmService.getAutos(true);
                if (autos.size()==0) {
                    return Result.SUCCESS;
                }
                Log.d(PenaltyCheckApplication.TAG, "Worker works!");
                networkService.checkPenalty(this, autos);
            } catch (Exception e) {
                Log.d(PenaltyCheckApplication.TAG, "Worker fails: " + e.getLocalizedMessage());
                return Result.FAILURE;
            }
            finally {
                realmService.closeRealm();
            }
            Log.d(PenaltyCheckApplication.TAG, "Worker success!");
            return Result.SUCCESS;
        }
        else {
            Log.d(PenaltyCheckApplication.TAG, "Worker retry!");
            return Result.RETRY;
        }
    }

    @Override
    public void onSuccessRequest(long count) {
        Log.d(PenaltyCheckApplication.TAG, "Worker request success!");
        if (count > 0){
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            Notification notification = notificationUtils.getNotification(count);
            notificationUtils.getManager().notify(NotificationUtils.NOTIFY_ID, notification);
        }
    }

    @Override
    public void onErrorRequest(String error) { ;
        Log.d(PenaltyCheckApplication.TAG, "Worker request error!");
    }

    public static PeriodicWorkRequest getCheckPenaltyWork() {
        Log.d(PenaltyCheckApplication.TAG, "Worker builder!");
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        PeriodicWorkRequest.Builder checkPenaltyWorkerBuilder =
                new PeriodicWorkRequest.Builder(CheckPenaltyWorker.class, 6, TimeUnit.HOURS)
                .setConstraints(constraints)
                .addTag(PenaltyCheckApplication.TAG);
        return checkPenaltyWorkerBuilder.build();
    }
}
