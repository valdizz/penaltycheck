package com.valdizz.penaltycheck.adapter;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.db.DataHelper;
import com.valdizz.penaltycheck.model.Penalty;

import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class PenaltyRecyclerViewAdapter extends RealmRecyclerViewAdapter<Penalty, PenaltyRecyclerViewAdapter.PenaltyViewHolder>{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");

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

        holder.penalty_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.delete_button.setOnClickListener(new View.OnClickListener() {
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
        holder.payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO payment methods
            }
        });
    }

    class PenaltyViewHolder extends RecyclerView.ViewHolder{

        CardView penalty_card;
        TextView resolutionnumber, penaltydate;
        ImageButton delete_button, payment_button;

        PenaltyViewHolder(View view){
            super(view);
            penalty_card = (CardView) view.findViewById(R.id.penalty_card);
            delete_button = (ImageButton) view.findViewById(R.id.delete_button);
            payment_button = (ImageButton) view.findViewById(R.id.payment_button);
            resolutionnumber = (TextView) view.findViewById(R.id.tv_resolutionnumber);
            penaltydate = (TextView) view.findViewById(R.id.tv_penaltydate);
        }
    }
}
