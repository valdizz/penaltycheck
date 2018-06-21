package com.valdizz.penaltycheck.mvp.autoactivity;


import com.valdizz.penaltycheck.model.NetworkService;
import com.valdizz.penaltycheck.model.NetworkServiceListener;
import com.valdizz.penaltycheck.model.RealmService;


import io.reactivex.disposables.CompositeDisposable;

public class AutoActivityPresenter implements AutoActivityContract.Presenter, NetworkServiceListener {

    private AutoActivityContract.View autoActivityView;
    private NetworkService networkService;
    private RealmService realmService;
    private CompositeDisposable disposables;

    public AutoActivityPresenter(AutoActivityContract.View autoActivityView, NetworkService networkService, RealmService realmService) {
        this.autoActivityView = autoActivityView;
        this.networkService = networkService;
        this.realmService = realmService;
        this.disposables = new CompositeDisposable();
    }

    @Override
    public void onAddAutoClick() {
        autoActivityView.showAddAuto();
    }

    @Override
    public void onDeleteAutoClick(long id) {
        realmService.deleteAuto(id);
    }

    @Override
    public void onEditAutoClick(long id) {
        autoActivityView.showEditAuto(id);
    }

    @Override
    public void onGetPenalties(long id) {
        autoActivityView.showPenalties(id);
    }

    @Override
    public void onCheckPenalties() {
        autoActivityView.showRefreshing(true);
        disposables.add(networkService.checkPenalty(this, realmService.getAutos(false)));
    }

    @Override
    public void onSuccessRequest(long count) {
        autoActivityView.showRefreshing(false);
        autoActivityView.showMessage(count);
    }

    @Override
    public void onErrorRequest(String error) {
        autoActivityView.showRefreshing(false);
        autoActivityView.showErrorMessage(error);
    }

    @Override
    public void onHelpClick() {
        autoActivityView.showHelp();
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }

    @Override
    public void onDispose() {
        disposables.dispose();
    }
}
