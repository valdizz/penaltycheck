package com.valdizz.penaltycheck.mvp.autoactivity;


import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.mvp.common.BasePresenter;

import java.util.List;

public interface AutoActivityContract {

    interface View{
        void showAddAuto();
        void showEditAuto(long id);
        void showPenalties(long id);
        void showHelp();
        void showRefreshing(boolean isRefresh);
        void showMessage(long count);
        void showErrorMessage(String error);
        void saveAuto();
    }

    interface Presenter extends BasePresenter{
        void onAddAutoClick();
        void onDeleteAutoClick(long id);
        void onEditAutoClick(long id);
        void onSaveAutoClick();
        void onGetPenalties(long id);
        void onCheckPenalties();
        void onHelpClick();
        void onDispose();
    }

}
