package com.valdizz.penaltycheck.mvp.helpactivity;


public interface HelpActivityContract {

    interface View{
        void sendEmail();
        void rateApp();
    }

    interface Presenter {
        void onSendEmailClick();
        void onRateAppClick();
    }
}
