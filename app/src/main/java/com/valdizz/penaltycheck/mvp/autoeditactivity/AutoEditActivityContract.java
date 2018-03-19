package com.valdizz.penaltycheck.mvp.autoeditactivity;


import com.valdizz.penaltycheck.mvp.common.BasePresenter;

public interface AutoEditActivityContract {

    interface View{
        void saveAuto();
    }

    interface Presenter extends BasePresenter{
        void onSaveAutoClick();
    }
}
