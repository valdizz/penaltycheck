package com.valdizz.penaltycheck.mvp.paymentactivity;


public interface PaymentActivityContract {

    interface View{
        void showRateApp();
        void showHelp();
    }

    interface Presenter {
        void onRateAppClick();
        void onHelpClick();
    }
}
