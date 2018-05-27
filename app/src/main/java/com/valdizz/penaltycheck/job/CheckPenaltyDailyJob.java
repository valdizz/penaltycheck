package com.valdizz.penaltycheck.job;

import android.app.Notification;
import android.support.annotation.NonNull;

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

    public static final String TAG = "CheckPenaltyDailyJob";
    private Disposable disposable;
    @Inject NetworkService networkService;

    public CheckPenaltyDailyJob() {
        PenaltyCheckApplication.getComponent().injectCheckPenaltyDailyJob(this);
    }

    public static void schedule() {
        if (!JobManager.instance().getAllJobRequestsForTag(TAG).isEmpty()) {
            return;
        }
        JobRequest.Builder builder = new JobRequest.Builder(TAG).setRequiredNetworkType(JobRequest.NetworkType.UNMETERED);
        DailyJob.schedule(builder, TimeUnit.HOURS.toMillis(12), TimeUnit.HOURS.toMillis(20));
    }

    @NonNull
    @Override
    protected DailyJobResult onRunDailyJob(@NonNull Params params) {
        if (CheckPermissionsUtils.isOnline(getContext())){
            RealmService realmService = new RealmService();
            try {
                List<Auto> autos = realmService.getAutos(true);
                disposable = networkService.checkPenalty(this, autos);
            } catch (Exception e) {
                return DailyJobResult.CANCEL;
            }
            finally {
                realmService.closeRealm();
            }
            return DailyJobResult.SUCCESS;
        }
        else {
            return DailyJobResult.CANCEL;
        }
    }

    @Override
    public void onSuccessRequest(long count) {
        if (count > 0){
            NotificationUtils notificationUtils = new NotificationUtils(getContext());
            Notification notification = notificationUtils.getNotification(count);
            notificationUtils.getManager().notify(NotificationUtils.NOTIFY_ID, notification);
        }
        disposable.dispose();
    }

    @Override
    public void onErrorRequest(String error) {
        disposable.dispose();
    }
}
