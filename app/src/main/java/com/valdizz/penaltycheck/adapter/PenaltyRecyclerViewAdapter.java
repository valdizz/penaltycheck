package com.valdizz.penaltycheck.adapter;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.db.DataHelper;
import com.valdizz.penaltycheck.model.Penalty;

import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class PenaltyRecyclerViewAdapter extends RealmRecyclerViewAdapter<Penalty, PenaltyRecyclerViewAdapter.PenaltyViewHolder>{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm:ss");

    public PenaltyRecyclerViewAdapter(OrderedRealmCollection<Penalty> data) {
        super(data, true);
    }

    @Override
    public PenaltyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_penalty, parent, false);
        return new PenaltyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PenaltyViewHolder holder, int position) {
        final Penalty penalty = getItem(position);
        holder.resolutionnumber.setText(penalty.getNumber());
        holder.penaltydate.setText(dateFormat.format(penalty.getDate()));
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(android.R.string.dialog_alert_title)
                        .setMessage(R.string.dialog_deletepenalty)
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DataHelper.deletePenalty(penalty);
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
    }

    class PenaltyViewHolder extends RecyclerView.ViewHolder{
        SwipeLayout swipeLayout;
        TextView resolutionnumber;
        TextView penaltydate;
        ImageView item_delete;

        PenaltyViewHolder(View view){
            super(view);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_penalty);
            item_delete = (ImageView) view.findViewById(R.id.item_penalty_delete);
            resolutionnumber = (TextView) view.findViewById(R.id.tvResolutionNumber);
            penaltydate = (TextView) view.findViewById(R.id.tvPenaltyDate);
        }
    }
}
