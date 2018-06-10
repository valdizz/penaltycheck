package com.valdizz.penaltycheck.mvp.paymentactivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.mvp.helpactivity.HelpActivity;
import com.valdizz.penaltycheck.mvp.penaltyfragment.PenaltyFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.valdizz.penaltycheck.model.RealmService.GOOGLEPLAY_URI;
import static com.valdizz.penaltycheck.model.RealmService.PENALTYID_PARAM;

public class PaymentActivity extends AppCompatActivity implements PaymentActivityContract.View{

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.payment_viewpager) ViewPager viewpagerPayment;
    @BindView(R.id.payment_tablayout) TabLayout tablayoutPayment;
    private PaymentActivityContract.Presenter paymentActivityPresenter;

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
        String penaltyNumber = getIntent().getStringExtra(PENALTYID_PARAM);
        viewpagerPayment.setAdapter(new PaymentFragmentPagerAdapter(getSupportFragmentManager(), penaltyNumber, this));
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
