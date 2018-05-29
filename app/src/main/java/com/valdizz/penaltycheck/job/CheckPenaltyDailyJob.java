package com.valdizz.penaltycheck.job;

import android.app.Notification;
import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.DailyJob;
import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;
import com.valdizz.penaltycheck.PenaltyCheckApplication;
import com.valdizz.penaltycheck.model.NetworkService;
import com.valdizz.penaltycheck.model.NetworkServiceListener;
import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.util.CheckPermissionsUtils;
import com.valdizz.penaltycheck.util.NotificationUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import io.reactivex.disposables.Disposable;

public class CheckPenaltyDailyJob extends DailyJob implements NetworkServiceListener{

    public static final String TAG = "PenaltyCheckDailyJob";
    private Disposable disposable;
    @Inject NetworkService networkService;

    public CheckPenaltyDailyJob() {
        Log.d(TAG, "Constructor");
        PenaltyCheckApplication.getComponent().injectCheckPenaltyDailyJob(this);
    }

    public static void schedule() {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            return;
        }
        Log.d(TAG, "Shedule");
        JobRequest.Builder builder = new JobRequest.Builder(TAG).setRequiredNetworkType(JobRequest.NetworkType.CONNECTED);
        DailyJob.schedule(builder, TimeUnit.HOURS.toMillis(23), TimeUnit.HOURS.toMillis(1)+TimeUnit.MINUTES.toMillis(10));
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        Log.d(TAG, "Start job");
        if (CheckPermissionsUtils.isOnline(getContext())){
            RealmService realmService = new RealmService();
            try {
                List<Auto> autos = realmService.getAutos(true);
                disposable = networkService.checkPenalty(this, autos);
                Log.d(TAG, "Check penalty");
            } catch (Exception e) {
                Log.d(TAG, "Cancel job");
                return DailyJobResult.CANCEL;
            }
            finally {
                realmService.closeRealm();
            }
            Log.d(TAG, "Success");
            return DailyJobResult.SUCCESS;
        }
        else {
            Log.d(TAG, "Cancel");
            return DailyJobResult.CANCEL;
        }
    }

    @Override
    public void onSuccessRequest(long count) {
        Log.d(TAG, "onSuccessRequest");
        if (count > 0){
            NotificationUtils notificationUtils = new NotificationUtils(getContext());
            Notification notification = notificationUtils.getNotification(count);
            notificationUtils.getManager().notify(NotificationUtils.NOTIFY_ID, notification);
        }
        disposable.dispose();
    }

    @Override
    public void onErrorRequest(String error) {
        Log.d(TAG, "onErrorRequest");
        disposable.dispose();
    }
}
