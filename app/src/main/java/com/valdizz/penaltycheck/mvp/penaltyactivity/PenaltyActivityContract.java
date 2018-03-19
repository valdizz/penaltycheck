package com.valdizz.penaltycheck.mvp.penaltyactivity;


import com.valdizz.penaltycheck.mvp.common.BasePresenter;

public interface PenaltyActivityContract {

    interface View{
        void showRateApp();
        void showHelp();
        void showRefreshing(boolean isRefresh);
        void showMessage(String text);
    }

    interface Presenter extends BasePresenter{
        void onCheckPenalties(long id);
        void onRateAppClick();
        void onHelpClick();
    }
}
