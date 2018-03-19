package com.valdizz.penaltycheck.mvp.penaltyfragment;


import com.valdizz.penaltycheck.model.entity.Penalty;
import com.valdizz.penaltycheck.mvp.common.BasePresenter;

public interface PenaltyFragmentContract {

    interface View{

    }

    interface Presenter extends BasePresenter{
        void onPayPenaltyClick(Penalty penalty);
        void onDeletePenaltyClick(Penalty penalty);
        void onViewPenaltyClick(Penalty penalty);
    }
}
