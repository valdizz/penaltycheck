package com.valdizz.penaltycheck;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.swipe.SwipeLayout;
import com.valdizz.penaltycheck.adapter.AutoRecyclerViewAdapter;
import com.valdizz.penaltycheck.db.DataHelper;
import com.valdizz.penaltycheck.model.Auto;

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
            //check operation
            checkPenalty();
        }
    };

    public void checkPenalty(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Snackbar.make(getView(), "Check action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        },2000);
    }

    private void setUpRecyclerView() {
        adapter = new AutoRecyclerViewAdapter(realm.where(Auto.class).findAllAsync().sort("id"));
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        //recyclerView.setItemAnimator();
    }


}
