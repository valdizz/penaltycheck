package com.valdizz.penaltycheck.mvp.penaltyactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.valdizz.penaltycheck.PenaltyCheckApplication;
import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.model.NetworkService;
import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.mvp.autoactivity.AutoActivity;
import com.valdizz.penaltycheck.mvp.helpactivity.HelpActivity;
import com.valdizz.penaltycheck.mvp.penaltyfragment.PenaltyFragment;
import com.valdizz.penaltycheck.util.CheckPermissionsUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.valdizz.penaltycheck.model.RealmService.AUTOID_PARAM;

public class PenaltyActivity extends AppCompatActivity implements PenaltyActivityContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.tvPFullname) TextView tvPFullname;
    @BindView(R.id.tvPCertificate) TextView tvPCertificate;
    @BindView(R.id.tvPDescription) TextView tvPDescription;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @Inject RealmService realmService;
    @Inject NetworkService networkService;
    private PenaltyActivityContract.Presenter penaltyActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penalty);
        PenaltyCheckApplication.getComponent().injectPenaltyActivity(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_activity_penalty);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (penaltyActivityPresenter == null){
            penaltyActivityPresenter = new PenaltyActivityPresenter(this, networkService, realmService);
        }
        initHeader(getIntent().getLongExtra(AUTOID_PARAM, -1));
        addFragment(getIntent());
    }

    @Override
    protected void onDestroy() {
        penaltyActivityPresenter.closeRealm();
        penaltyActivityPresenter.onDispose();
        super.onDestroy();
    }

    private void initHeader(long auto_id){
        Auto auto = realmService.getAuto(auto_id);
        tvPFullname.setText(auto.getFullName());
        tvPCertificate.setText(getString(R.string.label_certificate_short, auto.getSeries(), auto.getNumber()));
        tvPDescription.setText(auto.getDescription());
    }

    private void addFragment(Intent intent){
        PenaltyFragment penaltyFragment = new PenaltyFragment();
        if (intent.hasExtra(AUTOID_PARAM)){
            Bundle arguments = new Bundle();
            arguments.putLong(AUTOID_PARAM, intent.getLongExtra(AUTOID_PARAM, -1));
            penaltyFragment.setArguments(arguments);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.auto_detail_container, penaltyFragment).commit();
    }

    //check penalties for this auto
    @OnClick(R.id.fab)
    void checkPenaltiesClick(){
        if (CheckPermissionsUtils.isOnline(this)){
            penaltyActivityPresenter.onCheckPenalties(getIntent().getLongExtra(AUTOID_PARAM, -1));
        }
        else {
            Snackbar.make(fab, getString(R.string.dialog_checkinternet), Snackbar.LENGTH_LONG).show();
        }
    }

    //show help
    @Override
    public void showHelp() {
        startActivity(new Intent(this, HelpActivity.class));
    }

    @Override
    public void showRefreshing(boolean isRefresh) {
        progressBar.setVisibility(isRefresh ? ProgressBar.VISIBLE : ProgressBar.INVISIBLE);
    }

    @Override
    public void showMessage(long count) {
        Snackbar.make(fab, count > 0 ? getResources().getQuantityString(R.plurals.dialog_penaltyfound, (int)count, (int)count) : getString(R.string.dialog_penaltynotfound), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(String error) {
        Snackbar.make(fab, getString(R.string.dialog_networkerror, error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_check).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                navigateUpTo(new Intent(this, AutoActivity.class));
                return true;
            case R.id.action_help:
                penaltyActivityPresenter.onHelpClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
