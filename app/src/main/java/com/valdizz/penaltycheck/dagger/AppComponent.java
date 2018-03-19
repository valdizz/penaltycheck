package com.valdizz.penaltycheck.dagger;


import com.valdizz.penaltycheck.mvp.autoactivity.AutoActivity;
import com.valdizz.penaltycheck.mvp.autoeditactivity.AutoEditActivity;
import com.valdizz.penaltycheck.mvp.autoeditfragment.AutoEditFragment;
import com.valdizz.penaltycheck.mvp.penaltyactivity.PenaltyActivity;
import com.valdizz.penaltycheck.mvp.penaltyfragment.PenaltyFragment;

import dagger.Component;

@Component(modules = {StorageModule.class})
public interface AppComponent {

    void injectAutoActivity(AutoActivity autoActivity);
    void injectAutoEditActivity(AutoEditActivity autoEditActivity);
    void injectAutoEditFragment(AutoEditFragment autoEditFragment);
    void injectPenaltyActivity(PenaltyActivity penaltyActivity);
    void injectPenaltyFragment(PenaltyFragment penaltyFragment);
}
