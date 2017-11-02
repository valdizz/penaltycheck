package com.valdizz.penaltycheck;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valdizz.penaltycheck.adapter.AutoRecyclerViewAdapter;
import com.valdizz.penaltycheck.db.DataHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class AutoFragment extends Fragment {

    private Realm realm;
    private AutoRecyclerViewAdapter adapter;
    @BindView(R.id.recyclerview_auto) RecyclerView recyclerView;
    @BindView(R.id.swiperefresh_auto) SwipeRefreshLayout swipeRefreshLayout;

    public AutoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        realm = Realm.getDefaultInstance();

//        realm.beginTransaction();
//        Auto auto = realm.where(Auto.class).equalTo("id", 3).findFirst();
//
//            Penalty penalty = realm.createObject(Penalty.class);
//            penalty.setNumber("123123");
//            penalty.setDate(new Date());
//            penalty.setChecked(true);
//            ((Auto)auto).getPenalties().add(penalty);
//            Penalty penalty2 = realm.createObject(Penalty.class);
//            penalty2.setNumber("555444");
//            penalty2.setDate(new Date());
//            penalty2.setChecked(true);
//            ((Auto)auto).getPenalties().add(penalty2);
//            Penalty penalty3 = realm.createObject(Penalty.class);
//            penalty3.setNumber("4646546");
//            penalty3.setDate(new Date());
//            penalty3.setChecked(false);
//            ((Auto)auto).getPenalties().add(penalty3);
//
//        realm.commitTransaction();

        View view = inflater.inflate(R.layout.fragment_auto, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(swiperefreshListener);
        setUpRecyclerView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        recyclerView.setAdapter(null);
        realm.close();
    }

    private SwipeRefreshLayout.OnRefreshListener swiperefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            //check penalties for all autos
            checkPenalties();
        }
    };

    public void checkPenalties(){
        swipeRefreshLayout.setRefreshing(true);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(getView(), "Check penalties", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        },2000);
    }

    private void setUpRecyclerView() {
        adapter = new AutoRecyclerViewAdapter(DataHelper.getAutos(realm));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


}
