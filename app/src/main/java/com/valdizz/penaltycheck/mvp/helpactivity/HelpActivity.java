package com.valdizz.penaltycheck.mvp.helpactivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valdizz.penaltycheck.BuildConfig;
import com.valdizz.penaltycheck.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.valdizz.penaltycheck.model.RealmService.GOOGLEPLAY_URI;
import static com.valdizz.penaltycheck.model.RealmService.MAILTO;

public class HelpActivity extends AppCompatActivity implements HelpActivityContract.View {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_appversion) TextView tvAppVersion;
    private HelpActivityContract.Presenter helpActivityPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_activity_help);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (helpActivityPresenter == null){
            helpActivityPresenter = new HelpActivityPresenter(this);
        }
        tvAppVersion.setText(getString(R.string.label_version, BuildConfig.VERSION_NAME));
    }

    @OnClick(R.id.ll_sendemail)
    void onSendEmailClick(){
        helpActivityPresenter.onSendEmailClick();
    }

    @OnClick(R.id.ll_rateapp)
    void onRateAppClick(){
        helpActivityPresenter.onRateAppClick();
    }

    @Override
    public void sendEmail() {
        startActivity(new Intent(Intent.ACTION_SENDTO,  Uri.parse(MAILTO)));
    }

    @Override
    public void rateApp() {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(GOOGLEPLAY_URI)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
