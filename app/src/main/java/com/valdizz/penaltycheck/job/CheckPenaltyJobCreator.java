package com.valdizz.penaltycheck.job;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

public class CheckPenaltyJobCreator implements JobCreator {

    @Nullable
    @Override
    public Job create(@NonNull String tag) {
        switch (tag) {
            case CheckPenaltyDailyJob.TAG:
                return new CheckPenaltyDailyJob();
            default:
                return null;
        }
    }
}
