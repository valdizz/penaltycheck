package com.valdizz.penaltycheck.mvp.paymentactivity;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valdizz.penaltycheck.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.valdizz.penaltycheck.model.RealmService.PENALTYID_PARAM;


public class PaymentInfoFragment extends Fragment {

    @BindView(R.id.tv_help) TextView tvHelp;

    public PaymentInfoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_info, container, false);
        ButterKnife.bind(this, view);

        String penaltyNumber = "";
        if (getArguments() != null)
            penaltyNumber = getArguments().getString(PENALTYID_PARAM);
        tvHelp.setText(getString(R.string.big_text_payment, penaltyNumber));
        return view;
    }

}
