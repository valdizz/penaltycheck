package com.valdizz.penaltycheck.adapter;

;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class RecyclerViewEmptyObserver extends RecyclerView.AdapterDataObserver {

    private View view;
    private View emptyView;
    private RecyclerView recyclerView;

    public RecyclerViewEmptyObserver(RecyclerView rv, View emptyView, View view) {
        this.recyclerView = rv;
        this.emptyView = emptyView;
        this.view = view;
        checkIfEmpty();
    }

    private void checkIfEmpty() {
        if (emptyView != null && view != null && recyclerView.getAdapter() != null) {
            boolean emptyViewVisible = recyclerView.getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            view.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }

    public void onChanged() {
        checkIfEmpty();
    }

    public void onItemRangeInserted(int positionStart, int itemCount) {
        checkIfEmpty();
    }

    public void onItemRangeRemoved(int positionStart, int itemCount) {
        checkIfEmpty();
    }
}
