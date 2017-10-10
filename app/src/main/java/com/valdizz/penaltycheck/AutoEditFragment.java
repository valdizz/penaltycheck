package com.valdizz.penaltycheck;

import android.os.Bundle;
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

    private static final String AUTOID_PARAM = "auto_id";
    private long auto_id;
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
        args.putLong(AUTOID_PARAM, auto_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            auto_id = getArguments().getLong(AUTOID_PARAM);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autoedit, container, false);
        ButterKnife.bind(this, view);
        if (auto_id != -1){
            Auto auto = DataHelper.getAuto(auto_id);
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
        Auto auto = new Auto();
        if (auto_id != -1)
            auto.setId(auto_id);
        auto.setSurname(etSurname.getText().toString());
        auto.setName(etName.getText().toString());
        auto.setPatronymic(etPatronymic.getText().toString());
        auto.setSeries(etSeries.getText().toString());
        auto.setNumber(etNumber.getText().toString());
        auto.setDescription(etDescription.getText().toString());
        auto.setAutomatically(switchAutocheck.isChecked());
        if (auto_id != -1)
            DataHelper.updateAuto(auto);
        else
            DataHelper.createAuto(auto);
        getActivity().finish();
    }

}
