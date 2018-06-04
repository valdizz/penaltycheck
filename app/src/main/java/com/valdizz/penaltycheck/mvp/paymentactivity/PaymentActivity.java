package com.valdizz.penaltycheck.mvp.paymentactivity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.mvp.helpactivity.HelpActivity;
import com.valdizz.penaltycheck.mvp.penaltyfragment.PenaltyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.valdizz.penaltycheck.model.RealmService.GOOGLEPLAY_URI;
import static com.valdizz.penaltycheck.model.RealmService.PENALTYID_PARAM;

public class PaymentActivity extends AppCompatActivity implements PaymentActivityContract.View{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.tv_payment) TextView tvPayment;
    private PaymentActivityContract.Presenter paymentActivityPresenter;
    private String penaltyNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(R.string.title_activity_payment);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (paymentActivityPresenter == null){
            paymentActivityPresenter = new PaymentActivityPresenter(this);
        }
        penaltyNumber = getIntent().getStringExtra(PENALTYID_PARAM);
        tvPayment.setText(getString(R.string.big_text_payment, penaltyNumber));
    }

    @Override
    public void showRateApp() {
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(GOOGLEPLAY_URI)));
    }

    @Override
    public void showHelp() {
        startActivity(new Intent(this, HelpActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_check).setVisible(false);
        menu.findItem(R.id.action_save).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                navigateUpTo(new Intent(this, PenaltyFragment.class));
                return true;
            case R.id.action_help:
                paymentActivityPresenter.onHelpClick();
                return true;
            case R.id.action_rate:
                paymentActivityPresenter.onRateAppClick();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
