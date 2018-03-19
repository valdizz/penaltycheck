package com.valdizz.penaltycheck.mvp.penaltyactivity;


import android.os.Handler;

import com.valdizz.penaltycheck.model.RealmService;

import java.util.Date;
import java.util.Random;

public class PenaltyActivityPresenter implements PenaltyActivityContract.Presenter {

    private PenaltyActivityContract.View penaltyActivityView;
    private RealmService realmService;

    public PenaltyActivityPresenter(PenaltyActivityContract.View penaltyActivityView, RealmService realmService) {
        this.penaltyActivityView = penaltyActivityView;
        this.realmService = realmService;
    }

    @Override
    public void onCheckPenalties(final long id) {
        penaltyActivityView.showRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check penalties
                realmService.addPenalty(id, new Date(), "Test penalty "+new Random().nextInt(99));
                penaltyActivityView.showMessage("Check penalties!");
                penaltyActivityView.showRefreshing(false);
            }
        },2000);
    }

    @Override
    public void onRateAppClick() {
        penaltyActivityView.showRateApp();
    }

    @Override
    public void onHelpClick() {
        penaltyActivityView.showHelp();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }
}
