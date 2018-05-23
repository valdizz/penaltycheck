package com.valdizz.penaltycheck.mvp.penaltyactivity;


import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.mvp.common.BasePresenter;

public interface PenaltyActivityContract {

    interface View{
        void showRateApp();
        void showHelp();
        void showRefreshing(boolean isRefresh);
        void showMessage(long count);
        void showErrorMessage(String error);
    }

    interface Presenter extends BasePresenter{
        void onCheckPenalties(long id);
        void onRateAppClick();
        void onHelpClick();
        void onDispose();
    }

}
