package com.valdizz.penaltycheck.mvp.paymentactivity;

public class PaymentActivityPresenter implements PaymentActivityContract.Presenter {

    private PaymentActivityContract.View paymentActivityView;

    public PaymentActivityPresenter(PaymentActivityContract.View paymentActivityView) {
        this.paymentActivityView = paymentActivityView;
    }

    @Override
    public void onHelpClick() {
        paymentActivityView.showHelp();
    }
}
