package com.valdizz.penaltycheck.mvp.autoeditactivity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.valdizz.penaltycheck.PenaltyCheckApplication;
import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.mvp.autoactivity.AutoActivity;
import com.valdizz.penaltycheck.mvp.autoeditfragment.AutoEditFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.valdizz.penaltycheck.model.RealmService.AUTOID_PARAM;


public class AutoEditActivity extends AppCompatActivity implements AutoEditActivityContract.View{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @Inject RealmService realmService;
    private AutoEditActivityContract.Presenter autoEditActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autoedit);
        PenaltyCheckApplication.getComponent().injectAutoEditActivity(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getIntent().hasExtra(AUTOID_PARAM) ? R.string.title_activity_editauto : R.string.title_activity_addauto);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (autoEditActivityPresenter == null){
            autoEditActivityPresenter = new AutoEditActivityPresenter(this, realmService);
        }
        addFragment(getIntent());
    }

    @Override
    protected void onDestroy() {
        autoEditActivityPresenter.closeRealm();
        super.onDestroy();
    }

    private void addFragment(Intent intent){
        AutoEditFragment autoEditFragment = new AutoEditFragment();
        if (intent.hasExtra(AUTOID_PARAM)){
            Bundle arguments = new Bundle();
            arguments.putLong(AUTOID_PARAM, intent.getLongExtra(AUTOID_PARAM, -1));
            autoEditFragment.setArguments(arguments);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.auto_detail_container, autoEditFragment).commit();
    }

    @Override
    public void saveAuto() {
        AutoEditFragment autoEditFragment = (AutoEditFragment) getSupportFragmentManager().findFragmentById(R.id.auto_detail_container);
        autoEditFragment.onSaveAutoClick();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_autoedit, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                navigateUpTo(new Intent(this, AutoActivity.class));
                return true;
            case R.id.action_save:
                autoEditActivityPresenter.onSaveAutoClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
