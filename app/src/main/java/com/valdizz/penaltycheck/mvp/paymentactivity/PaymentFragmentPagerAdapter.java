package com.valdizz.penaltycheck.mvp.paymentactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.valdizz.penaltycheck.R;

import static com.valdizz.penaltycheck.model.RealmService.PENALTYID_PARAM;

public class PaymentFragmentPagerAdapter extends FragmentPagerAdapter {

    private String penaltyNumber;
    private Context context;

    public PaymentFragmentPagerAdapter(FragmentManager fm, String penaltyNumber, Context context) {
        super(fm);
        this.penaltyNumber = penaltyNumber;
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putString(PENALTYID_PARAM, penaltyNumber);
        switch (position){
            case 0:
                PaymentInfoFragment paymentInfoFragment = new PaymentInfoFragment();
                paymentInfoFragment.setArguments(bundle);
                return paymentInfoFragment;
            case 1:
                PaymentPayFragment paymentPayFragment = new PaymentPayFragment();
                paymentPayFragment.setArguments(bundle);
                return paymentPayFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return context.getString(R.string.label_tab_info);
            case 1:
                return context.getString(R.string.label_tab_payment);
        }
        return null;
    }
}
