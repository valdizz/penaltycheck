package com.valdizz.penaltycheck.mvp.autoactivity;


import com.valdizz.penaltycheck.mvp.common.BasePresenter;

public interface AutoActivityContract {

    interface View{
        void showAddAuto();
        void showEditAuto(long id);
        void showPenalties(long id);
        void showRateApp();
        void showHelp();
        void showRefreshing(boolean isRefresh);
        void showMessage(String text);
        void saveAuto();
    }

    interface Presenter extends BasePresenter{
        void onAddAutoClick();
        void onDeleteAutoClick(long id);
        void onEditAutoClick(long id);
        void onSaveAutoClick();
        void onGetPenalties(long id);
        void onCheckPenalties();
        void onRateAppClick();
        void onHelpClick();
    }
}
