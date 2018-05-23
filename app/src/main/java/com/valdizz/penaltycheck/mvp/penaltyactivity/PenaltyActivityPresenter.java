package com.valdizz.penaltycheck.mvp.penaltyactivity;


import android.os.Handler;
import android.os.Looper;

import com.valdizz.penaltycheck.model.NetworkService;
import com.valdizz.penaltycheck.model.NetworkServiceListener;
import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Auto;

import java.util.Arrays;
import java.util.Date;

import io.reactivex.disposables.CompositeDisposable;

public class PenaltyActivityPresenter implements PenaltyActivityContract.Presenter, NetworkServiceListener {

    private PenaltyActivityContract.View penaltyActivityView;
    private NetworkService networkService;
    private RealmService realmService;
    private CompositeDisposable disposables;

    public PenaltyActivityPresenter(PenaltyActivityContract.View penaltyActivityView, NetworkService networkService, RealmService realmService) {
        this.penaltyActivityView = penaltyActivityView;
        this.networkService = networkService;
        this.realmService = realmService;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void onCheckPenalties(long id) {
        penaltyActivityView.showRefreshing(true);
        disposables.add(networkService.checkPenalty(this, Arrays.asList(realmService.getAuto(id))));
    }

    @Override
    public void onFoundPenalty(Auto auto, Date date, String number) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                realmService.addPenalty(auto.getId(), date, number);
            }
        });
    }

    @Override
    public void onSetLastCheckDate(Auto auto, Date lastupdate) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                realmService.updateLastCheckDateAuto(auto.getId(), lastupdate);
            }
        });
    }

    @Override
    public void onSuccessRequest(long count) {
        penaltyActivityView.showRefreshing(false);
        penaltyActivityView.showMessage(count);
    }

    @Override
    public void onErrorRequest(String error) {
        penaltyActivityView.showRefreshing(false);
        penaltyActivityView.showErrorMessage(error);
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

    @Override
    public void onDispose() {
        disposables.dispose();
    }
}
