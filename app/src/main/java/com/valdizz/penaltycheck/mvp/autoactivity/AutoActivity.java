package com.valdizz.penaltycheck.mvp.autoactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.valdizz.penaltycheck.PenaltyCheckApplication;
import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.adapter.AutoRecyclerViewAdapter;
import com.valdizz.penaltycheck.adapter.RecyclerItemTouchHelper;
import com.valdizz.penaltycheck.adapter.RecyclerViewEmptyObserver;
import com.valdizz.penaltycheck.model.NetworkService;
import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.mvp.autoeditactivity.AutoEditActivity;
import com.valdizz.penaltycheck.mvp.autoeditfragment.AutoEditFragment;
import com.valdizz.penaltycheck.mvp.penaltyactivity.PenaltyActivity;
import com.valdizz.penaltycheck.mvp.penaltyfragment.PenaltyFragment;
import com.valdizz.penaltycheck.util.CheckPermissionsUtils;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.valdizz.penaltycheck.model.RealmService.AUTOID_PARAM;
import static com.valdizz.penaltycheck.model.RealmService.GOOGLEPLAY_URI;

public class AutoActivity extends AppCompatActivity implements AutoActivityContract.View, AutoRecyclerViewAdapter.OnAutoClickListener {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.recyclerview_auto) RecyclerView recyclerView;
    @BindView(R.id.recyclerview_auto_isempty) LinearLayout emptyView;
    @BindView(R.id.swiperefresh_auto) SwipeRefreshLayout swipeRefreshLayout;
    private boolean mTwoPane;
    private AutoRecyclerViewAdapter recyclerViewAdapter;
    @Inject RealmService realmService;
    @Inject NetworkService networkService;
    private AutoActivityContract.Presenter autoActivityPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto);
        PenaltyCheckApplication.getComponent().injectAutoActivity(this);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.auto_detail_container) != null) {
            mTwoPane = true;
        }

        if (autoActivityPresenter == null) {
            autoActivityPresenter = new AutoActivityPresenter(this, networkService, realmService);
        }

        swipeRefreshLayout.setOnRefreshListener(swiperefreshListener);
        setupAutoRecyclerView();
    }

    @Override
    protected void onDestroy() {
        recyclerView.setAdapter(null);
        autoActivityPresenter.closeRealm();
        autoActivityPresenter.onDispose();
        super.onDestroy();
    }

    private void setupAutoRecyclerView() {
        recyclerViewAdapter = new AutoRecyclerViewAdapter(realmService.getAutos());
        recyclerViewAdapter.setAutoClickListener(this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.registerAdapterDataObserver(new RecyclerViewEmptyObserver(recyclerView, emptyView, swipeRefreshLayout));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, recyclerViewAdapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    //add auto
    @OnClick(R.id.fab)
    public void addAutoClick() {
        autoActivityPresenter.onAddAutoClick();
    };

    @Override
    public void showAddAuto() {
        if (mTwoPane){
            getSupportFragmentManager().beginTransaction().replace(R.id.auto_detail_container, new AutoEditFragment()).addToBackStack(null).commit();
        }
        else {
            startActivity(new Intent(this, AutoEditActivity.class));
        }
        invalidateOptionsMenu();
    }

    //edit auto
    @Override
    public void editAutoClick(long id) {
        autoActivityPresenter.onEditAutoClick(id);
    }

    @Override
    public void showEditAuto(long id) {
        if (mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putLong(AUTOID_PARAM, id);
            AutoEditFragment autoEditFragment = new AutoEditFragment();
            autoEditFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.auto_detail_container, autoEditFragment).addToBackStack(null).commit();
        }
        else {
            startActivity(new Intent(this, AutoEditActivity.class).putExtra(AUTOID_PARAM, id));
        }
        invalidateOptionsMenu();
    }

    //delete auto
    @Override
    public void deleteAutoClick(long id) {
        autoActivityPresenter.onDeleteAutoClick(id);
    }

    @Override
    public void saveAuto() {
        AutoEditFragment autoEditFragment = (AutoEditFragment) getSupportFragmentManager().findFragmentById(R.id.auto_detail_container);
        autoEditFragment.onSaveAutoClick();
    }

    //get penalties for this auto
    @Override
    public void getPenaltiesClick(long id) {
        autoActivityPresenter.onGetPenalties(id);
    }

    @Override
    public void showPenalties(long id) {
        if (mTwoPane){
            Bundle arguments = new Bundle();
            arguments.putLong(AUTOID_PARAM, id);
            PenaltyFragment penaltyFragment = new PenaltyFragment();
            penaltyFragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction().replace(R.id.auto_detail_container, penaltyFragment).commit();
        }
        else {
            startActivity(new Intent(this, PenaltyActivity.class).putExtra(AUTOID_PARAM, id));
        }
    }

    //check penalties
    private SwipeRefreshLayout.OnRefreshListener swiperefreshListener = () -> checkPenalties();

    private void checkPenalties(){
        if (CheckPermissionsUtils.isOnline(this)){
            autoActivityPresenter.onCheckPenalties();
        }
        else {
            showRefreshing(false);
            Snackbar.make(swipeRefreshLayout, getString(R.string.dialog_checkinternet), Snackbar.LENGTH_LONG).show();
        }
    }

    //rate app
    @Override
    public void showRateApp() {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(GOOGLEPLAY_URI)));
    }

    //show help
    @Override
    public void showHelp() {
        //todo
    }

    @Override
    public void showRefreshing(boolean isRefresh) {
        swipeRefreshLayout.setRefreshing(isRefresh);
    }

    @Override
    public void showMessage(long count) {
        Snackbar.make(swipeRefreshLayout, count > 0 ? getResources().getQuantityString(R.plurals.dialog_penaltyfound, (int)count, (int)count) : getString(R.string.dialog_penaltynotfound), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showErrorMessage(String error) {
        Snackbar.make(swipeRefreshLayout, getString(R.string.dialog_networkerror, error), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item_check = menu.findItem(R.id.action_check);
        MenuItem item_help = menu.findItem(R.id.action_help);
        MenuItem item_rate = menu.findItem(R.id.action_rate);
        MenuItem item_save = menu.findItem(R.id.action_save);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.auto_detail_container);
        if (fragment != null && fragment instanceof AutoEditFragment){
            item_check.setVisible(false);
            item_help.setVisible(false);
            item_rate.setVisible(false);
            item_save.setVisible(true);
        }
        else if (fragment != null && fragment instanceof PenaltyFragment){
            item_check.setVisible(false);
            item_help.setVisible(true);
            item_rate.setVisible(true);
            item_save.setVisible(false);
    }
        else {
            item_check.setVisible(true);
            item_help.setVisible(true);
            item_rate.setVisible(true);
            item_save.setVisible(false);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_check:
                checkPenalties();
                return true;
            case R.id.action_help:
                autoActivityPresenter.onHelpClick();
                return true;
            case R.id.action_rate:
                autoActivityPresenter.onRateAppClick();
                return true;
            case R.id.action_save:
                autoActivityPresenter.onSaveAutoClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
