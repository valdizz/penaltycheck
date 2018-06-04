package com.valdizz.penaltycheck.mvp.helpactivity;

public class HelpActivityPresenter implements HelpActivityContract.Presenter {

    private HelpActivityContract.View helpActivityView;

    public HelpActivityPresenter(HelpActivityContract.View helpActivityView) {
        this.helpActivityView = helpActivityView;
    }

    @Override
    public void onRateAppClick() {
        helpActivityView.showRateApp();
    }
}
