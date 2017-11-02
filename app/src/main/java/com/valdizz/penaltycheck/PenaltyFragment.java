package com.valdizz.penaltycheck;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valdizz.penaltycheck.adapter.PenaltyRecyclerViewAdapter;
import com.valdizz.penaltycheck.db.DataHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class PenaltyFragment extends Fragment {

    private long auto_id;
    private Realm realm;
    private PenaltyRecyclerViewAdapter padapter;
    @BindView(R.id.recyclerview_penalty) RecyclerView penaltyRecyclerView;

    public PenaltyFragment() {
    }

    public static PenaltyFragment newInstance(long auto_id) {
        PenaltyFragment fragment = new PenaltyFragment();
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
        realm = Realm.getDefaultInstance();
        View view = inflater.inflate(R.layout.fragment_penalty, container, false);
        ButterKnife.bind(this, view);
        setupPenaltyRecyclerView(auto_id);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        penaltyRecyclerView.setAdapter(null);
        realm.close();
    }

    public void checkPenalty(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(getView(), "Check penalty", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        },2000);
    }

    private void setupPenaltyRecyclerView(long auto_id) {
        padapter = new PenaltyRecyclerViewAdapter(DataHelper.getPenalties(realm, auto_id));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        penaltyRecyclerView.setLayoutManager(layoutManager);
        penaltyRecyclerView.setNestedScrollingEnabled(false);
        penaltyRecyclerView.setAdapter(padapter);
        penaltyRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }
}
