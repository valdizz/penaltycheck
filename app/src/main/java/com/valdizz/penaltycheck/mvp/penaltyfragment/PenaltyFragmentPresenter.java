package com.valdizz.penaltycheck.mvp.penaltyfragment;


import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Penalty;

public class PenaltyFragmentPresenter implements PenaltyFragmentContract.Presenter{

    private PenaltyFragmentContract.View penaltyFragmentView;
    private RealmService realmService;

    public PenaltyFragmentPresenter(PenaltyFragmentContract.View penaltyFragmentView, RealmService realmService) {
        this.penaltyFragmentView = penaltyFragmentView;
        this.realmService = realmService;
    }

    @Override
    public void onPayPenaltyClick(Penalty penalty) {
        //todo
    }

    @Override
    public void onDeletePenaltyClick(Penalty penalty) {
        realmService.deletePenalty(penalty);
    }

    @Override
    public void onViewPenaltyClick(Penalty penalty) {
        realmService.viewPenalty(penalty);
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }
}
