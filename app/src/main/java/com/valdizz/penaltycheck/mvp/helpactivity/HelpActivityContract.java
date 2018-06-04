package com.valdizz.penaltycheck.mvp.helpactivity;


public interface HelpActivityContract {

    interface View{
        void showRateApp();
    }

    interface Presenter {
        void onRateAppClick();
    }
}
