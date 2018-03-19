package com.valdizz.penaltycheck.mvp.autoeditfragment;


import android.graphics.Bitmap;

import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Auto;

public class AutoEditFragmentPresenter implements AutoEditFragmentContract.Presenter{

    private AutoEditFragmentContract.View autoEditFragmentView;
    private RealmService realmService;

    public AutoEditFragmentPresenter(AutoEditFragmentContract.View autoEditFragmentView, RealmService realmService) {
        this.autoEditFragmentView = autoEditFragmentView;
        this.realmService = realmService;
    }

    @Override
    public void onSaveAutoClick(Auto auto) {
        //if auto with this certificate already exists
        if (realmService.checkAuto(auto.getId(), auto.getSeries(), auto.getNumber())){
            autoEditFragmentView.showErrorIfAutoExists();
            return;
        }

        //check field completion
        if (auto.getSurname().length()==0 || auto.getName().length()==0 || auto.getSeries().length()==0 || auto.getNumber().length()==0){
            autoEditFragmentView.showErrorFieldIsEmpty();
            return;
        }

        if (auto.getId() != 0){
            realmService.updateAuto(auto.getId(), auto.getSurname(), auto.getName(), auto.getPatronymic(), auto.getSeries(), auto.getNumber(), auto.getDescription(), auto.isAutomatically(), auto.getImage());
        }
        else {
            realmService.addAuto(auto.getSurname(), auto.getName(), auto.getPatronymic(), auto.getSeries(), auto.getNumber(), auto.getDescription(), auto.isAutomatically(), auto.getImage());
        }

        autoEditFragmentView.onClose();
    }

    @Override
    public void onShowCameraClick() {
        autoEditFragmentView.showCamera();
    }

    @Override
    public void onShowGalleryClick() {
        autoEditFragmentView.showGallery();
    }

    @Override
    public void onDeleteImageClick() {
        autoEditFragmentView.deleteCarPhoto();
    }

    @Override
    public void onSelectFromCameraResult(Bitmap bitmap) {
        autoEditFragmentView.showCarPhoto(bitmap);
    }

    @Override
    public void onSelectFromGalleryResult(Bitmap bitmap) {
        autoEditFragmentView.showCarPhoto(bitmap);
    }

    @Override
    public void closeRealm() {
        realmService.closeRealm();
    }
}
