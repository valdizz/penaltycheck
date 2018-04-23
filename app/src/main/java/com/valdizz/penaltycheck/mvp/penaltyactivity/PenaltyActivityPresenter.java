package com.valdizz.penaltycheck.mvp.penaltyactivity;


import android.os.Handler;

import com.valdizz.penaltycheck.model.NetworkService;
import com.valdizz.penaltycheck.model.RealmService;

import java.util.Date;
import java.util.Random;

public class PenaltyActivityPresenter implements PenaltyActivityContract.Presenter, PenaltyActivityContract.NetworkServiceListener {

    private PenaltyActivityContract.View penaltyActivityView;
    private NetworkService networkService;

    public PenaltyActivityPresenter(PenaltyActivityContract.View penaltyActivityView, NetworkService networkService) {
        this.penaltyActivityView = penaltyActivityView;
        this.networkService = networkService;
    }

    @Override
    public void onCheckPenalties(long id, final String fullname, final String series, final String number) {
        penaltyActivityView.showRefreshing(true);
        try {
            networkService.checkPenalty(this, id, fullname, series, number);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinishedNetworkService(boolean isPenaltyFound) {
        penaltyActivityView.showRefreshing(false);
        penaltyActivityView.showMessage(isPenaltyFound);
    }

    @Override
    public void onRateAppClick() {
        penaltyActivityView.showRateApp();
    }

    @Override
    public void onHelpClick() {
        penaltyActivityView.showHelp();
    }


}
