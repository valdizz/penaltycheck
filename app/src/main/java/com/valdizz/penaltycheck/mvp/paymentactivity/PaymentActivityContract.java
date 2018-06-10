package com.valdizz.penaltycheck.mvp.paymentactivity;


public interface PaymentActivityContract {

    interface View{
        void showHelp();
    }

    interface Presenter {
        void onHelpClick();
    }
}
