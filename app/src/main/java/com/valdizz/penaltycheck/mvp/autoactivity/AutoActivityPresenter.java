package com.valdizz.penaltycheck.mvp.autoactivity;


import android.os.Handler;

import com.valdizz.penaltycheck.model.RealmService;

public class AutoActivityPresenter implements AutoActivityContract.Presenter{

    private AutoActivityContract.View autoActivityView;
    private RealmService realmService;

    public AutoActivityPresenter(AutoActivityContract.View autoActivityView, RealmService realmService) {
        this.autoActivityView = autoActivityView;
        this.realmService = realmService;
    }

    @Override
    public void onAddAutoClick() {
        autoActivityView.showAddAuto();
    }

    @Override
    public void onDeleteAutoClick(long id) {
        realmService.deleteAuto(id);
    }

    @Override
    public void onEditAutoClick(long id) {
        autoActivityView.showEditAuto(id);
    }

    @Override
    public void onSaveAutoClick() {
        autoActivityView.saveAuto();
    }

    @Override
    public void onGetPenalties(long id) {
        autoActivityView.showPenalties(id);
    }

    @Override
    public void onCheckPenalties() {
        autoActivityView.showRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //check penalties
                autoActivityView.showMessage("Check penalties!");
                autoActivityView.showRefreshing(false);
            }
        },2000);
    }

    @Override
    public void onRateAppClick() {
        autoActivityView.showRateApp();
    }

    @Override
    public void onHelpClick() {
        autoActivityView.showHelp();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }
}
