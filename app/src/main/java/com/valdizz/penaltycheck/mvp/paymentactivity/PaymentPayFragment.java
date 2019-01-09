package com.valdizz.penaltycheck.mvp.paymentactivity;


import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_pay, container, false);
        ButterKnife.bind(this, view);

        String penaltyNumber = "";
        if (getArguments() != null)
            penaltyNumber = getArguments().getString(PENALTYID_PARAM);
        tvPenaltyNumber.setText(penaltyNumber);
        return view;
    }

    @OnClick(R.id.iv_penaltynumbercopy)
    void onPenaltyNumberCopyClick() {
        ClipboardManager clipboard = (ClipboardManager)getView().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(CLIP_LABEL, tvPenaltyNumber.getText());
        clipboard.setPrimaryClip(clip);
        Snackbar.make(getView(), getString(R.string.label_penaltynumber_clip), Snackbar.LENGTH_SHORT).show();
    }

    @OnClick({R.id.ll_epay, R.id.ll_ipay, R.id.ll_webpay, R.id.ll_apbbank, R.id.ll_asbbank, R.id.ll_bsbbank, R.id.ll_vebbank, R.id.ll_bnbbank, R.id.ll_mtbbank, R.id.ll_bpsbank, R.id.ll_stbbank,
            R.id.ll_prtbank, R.id.ll_mmbbank, R.id.ll_alfbank, R.id.ll_pribank, R.id.ll_vtbbank, R.id.ll_ztbbank, R.id.ll_bgpbank, R.id.ll_btabank, R.id.ll_tehbank, R.id.ll_idebank, R.id.ll_frbbank, R.id.ll_bibbank, R.id.ll_rrbbank, R.id.ll_rebbank})
    public void onBankingClick(View view) {
        PackageManager packageManager = getContext().getPackageManager();
        String[] banking = view.getTag().toString().split("\\|");
        Intent mobileBanking = packageManager.getLaunchIntentForPackage(banking[0]);
        Intent inetBanking = new Intent(Intent.ACTION_VIEW, Uri.parse(banking[1]));
        if (mobileBanking != null) {
            //choice of internet banking or mobile
            View dialogBankingView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_banking, null);
            final AlertDialog dialogBanking = getBankingChoiceDialog(dialogBankingView);

            LinearLayout llMobileBanking = dialogBankingView.findViewById(R.id.dialog_mobilebanking);
            llMobileBanking.setOnClickListener(v -> {
                dialogBanking.dismiss();
                startMobileBanking(mobileBanking);
            });

            LinearLayout llInternetBanking = dialogBankingView.findViewById(R.id.dialog_inetbanking);
            llInternetBanking.setOnClickListener(v -> {
                dialogBanking.dismiss();
                startInternetBanking(inetBanking, packageManager);
            });
            dialogBanking.show();
        }
        else {
            //start internet banking
            startInternetBanking(inetBanking, packageManager);
        }
    }

    private void startInternetBanking(Intent intent, PackageManager packageManager) {
        if (intent.resolveActivity(packageManager) != null)
            startActivity(intent);
        else
            Snackbar.make(tvPenaltyNumber, getString(R.string.error_openurl, intent.getData()), Snackbar.LENGTH_LONG).show();
    }

    private void startMobileBanking(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private AlertDialog getBankingChoiceDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
            .setCancelable(true)
            .setTitle(getString(R.string.dialog_paymentoption))
            .setView(view);
        return builder.create();
    }

}
