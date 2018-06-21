package com.valdizz.penaltycheck.mvp.paymentactivity;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.valdizz.penaltycheck.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.valdizz.penaltycheck.model.RealmService.PENALTYID_PARAM;


public class PaymentPayFragment extends Fragment {

    @BindView(R.id.tv_penaltynumber) TextView tvPenaltyNumber;
    private static final String CLIP_LABEL = "penaltyNumber";

    public PaymentPayFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_pay, container, false);
        ButterKnife.bind(this, view);

        String penaltyNumber = "";
        if (getArguments() != null)
            penaltyNumber = getArguments().getString(PENALTYID_PARAM);
        tvPenaltyNumber.setText(penaltyNumber);
        return view;
    }

    @OnClick(R.id.iv_penaltynumbercopy)
    void onPenaltyNumberCopyClick(){
        ClipboardManager clipboard = (ClipboardManager)getView().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(CLIP_LABEL, tvPenaltyNumber.getText());
        clipboard.setPrimaryClip(clip);
        Snackbar.make(getView(), getString(R.string.label_penaltynumber_clip), Snackbar.LENGTH_SHORT).show();
    }

}
