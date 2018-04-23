package com.valdizz.penaltycheck.mvp.penaltyactivity;


public interface PenaltyActivityContract {

    interface View{
        void showRateApp();
        void showHelp();
        void showRefreshing(boolean isRefresh);
        void showMessage(boolean isPenaltyFound);
    }

    interface Presenter{
        void onCheckPenalties(long id, String fullname, String series, String number);
        void onRateAppClick();
        void onHelpClick();
    }

    interface NetworkServiceListener{
        void onFinishedNetworkService(boolean isPenaltyFound);
    }
}
