package com.valdizz.penaltycheck.mvp.autoeditactivity;


import com.valdizz.penaltycheck.model.RealmService;

public class AutoEditActivityPresenter implements AutoEditActivityContract.Presenter {

    private AutoEditActivityContract.View autoEditActivityView;
    private RealmService realmService;

    public AutoEditActivityPresenter(AutoEditActivityContract.View autoEditActivityView, RealmService realmService) {
        this.autoEditActivityView = autoEditActivityView;
        this.realmService = realmService;
    }

    @Override
    public void onSaveAutoClick() {
        autoEditActivityView.saveAuto();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }
}
