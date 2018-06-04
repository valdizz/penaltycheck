package com.valdizz.penaltycheck.mvp.penaltyfragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.valdizz.penaltycheck.PenaltyCheckApplication;
import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.adapter.PenaltyRecyclerViewAdapter;
import com.valdizz.penaltycheck.adapter.RecyclerItemTouchHelper;
import com.valdizz.penaltycheck.adapter.RecyclerViewEmptyObserver;
import com.valdizz.penaltycheck.model.RealmService;
import com.valdizz.penaltycheck.model.entity.Penalty;
import com.valdizz.penaltycheck.mvp.paymentactivity.PaymentActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.valdizz.penaltycheck.model.RealmService.AUTOID_PARAM;
import static com.valdizz.penaltycheck.model.RealmService.PENALTYID_PARAM;

public class PenaltyFragment extends Fragment implements PenaltyFragmentContract.View, PenaltyRecyclerViewAdapter.OnPenaltyClickListener {

    @BindView(R.id.recyclerview_penalty) RecyclerView penaltyRecyclerView;
    @BindView(R.id.recyclerview_penalty_isempty) LinearLayout emptyView;
    @Inject RealmService realmService;
    private PenaltyRecyclerViewAdapter penaltyRecyclerViewAdapter;
    private PenaltyFragmentContract.Presenter penaltyFragmentPresenter;

    public PenaltyFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_penalty, container, false);
        PenaltyCheckApplication.getComponent().injectPenaltyFragment(this);
        ButterKnife.bind(this, view);

        if (penaltyFragmentPresenter == null) {
            penaltyFragmentPresenter = new PenaltyFragmentPresenter(this, realmService);
        }

        if (getArguments() != null) {
            setupPenaltyRecyclerView(getArguments().getLong(AUTOID_PARAM));
        }
        return view;
    }

    @Override
    public void onDestroy() {
        penaltyRecyclerView.setAdapter(null);
        penaltyFragmentPresenter.closeRealm();
        super.onDestroy();
    }

    private void setupPenaltyRecyclerView(long auto_id) {
        penaltyRecyclerViewAdapter = new PenaltyRecyclerViewAdapter(realmService.getPenalties(auto_id));
        penaltyRecyclerViewAdapter.setPenaltyClickListener(this);
        penaltyRecyclerView.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        penaltyRecyclerView.setLayoutManager(layoutManager);
        penaltyRecyclerView.setItemAnimator(new DefaultItemAnimator());
        penaltyRecyclerView.setAdapter(penaltyRecyclerViewAdapter);
        penaltyRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerViewEmptyObserver(penaltyRecyclerView, emptyView, penaltyRecyclerView));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, penaltyRecyclerViewAdapter));
        itemTouchHelper.attachToRecyclerView(penaltyRecyclerView);
    }

    @Override
    public void payPenaltyClick(Penalty penalty) {
        penaltyFragmentPresenter.onPayPenaltyClick(penalty);
    }

    @Override
    public void showPayment(Penalty penalty) {
        startActivity(new Intent(getActivity(), PaymentActivity.class).putExtra(PENALTYID_PARAM, penalty.getNumber()));
    }

    @Override
    public void deletePenaltyClick(Penalty penalty) {
        penaltyFragmentPresenter.onDeletePenaltyClick(penalty);
    }

    @Override
    public void viewPenaltyClick(Penalty penalty) {
        penaltyFragmentPresenter.onViewPenaltyClick(penalty);
    }
}
