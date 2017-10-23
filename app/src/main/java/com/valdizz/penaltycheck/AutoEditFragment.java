package com.valdizz.penaltycheck;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valdizz.penaltycheck.db.DataHelper;
import com.valdizz.penaltycheck.model.Auto;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AutoEditFragment extends Fragment {

    private long auto_id;
    private Auto auto;
    @BindView(R.id.etSurname) TextInputEditText etSurname;
    @BindView(R.id.etName) TextInputEditText etName;
    @BindView(R.id.etPatronymic) TextInputEditText etPatronymic;
    @BindView(R.id.etSeries) TextInputEditText etSeries;
    @BindView(R.id.etNumber) TextInputEditText etNumber;
    @BindView(R.id.etDescription) TextInputEditText etDescription;
    @BindView(R.id.switchAutocheck) SwitchCompat switchAutocheck;

    public AutoEditFragment() {
    }

    public static AutoEditFragment newInstance(long auto_id) {
        AutoEditFragment fragment = new AutoEditFragment();
        Bundle args = new Bundle();
        args.putLong(DataHelper.AUTOID_PARAM, auto_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            auto_id = getArguments().getLong(DataHelper.AUTOID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autoedit, container, false);
        ButterKnife.bind(this, view);
        if (auto_id != -1){
            auto = DataHelper.getAuto(auto_id);
            etSurname.setText(auto.getSurname());
            etName.setText(auto.getName());
            etPatronymic.setText(auto.getPatronymic());
            etSeries.setText(auto.getSeries());
            etNumber.setText(auto.getNumber());
            etDescription.setText(auto.getDescription());
            switchAutocheck.setChecked(auto.isAutomatically());
        }
        return view;
    }

    @OnClick(R.id.btnOk)
    public void saveAuto() {
        //if auto with this certificate already exists
        if (DataHelper.checkAuto(auto_id, etSeries.getText().toString(), etNumber.getText().toString())){
            Snackbar.make(getView(), getString(R.string.dialog_autoexists), Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return;
        }

        if (auto_id != -1)
            DataHelper.updateAuto(auto_id, etSurname.getText().toString(), etName.getText().toString(), etPatronymic.getText().toString(), etSeries.getText().toString(), etNumber.getText().toString(), etDescription.getText().toString(), switchAutocheck.isChecked());
        else
            DataHelper.createAuto(etSurname.getText().toString(), etName.getText().toString(), etPatronymic.getText().toString(), etSeries.getText().toString(), etNumber.getText().toString(), etDescription.getText().toString(), switchAutocheck.isChecked());
        getActivity().finish();
    }

}
