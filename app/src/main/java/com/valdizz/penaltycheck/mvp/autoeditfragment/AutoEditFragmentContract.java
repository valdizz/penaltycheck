package com.valdizz.penaltycheck.mvp.autoeditfragment;


import android.graphics.Bitmap;

import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.mvp.common.BasePresenter;

public interface AutoEditFragmentContract {

    interface View{
        void showErrorIfAutoExists();
        void showErrorFieldIsEmpty();
        void showCamera();
        void showGallery();
        void showCarPhoto(Bitmap bitmap);
        void deleteCarPhoto();
        void onSaveAutoClick();
        void onClose();
    }

    interface Presenter extends BasePresenter{
        void onSaveAutoClick(Auto auto);
        void onShowCameraClick();
        void onShowGalleryClick();
        void onDeleteImageClick();
        void onSelectFromCameraResult(Bitmap bitmap);
        void onSelectFromGalleryResult(Bitmap bitmap);
    }
}
