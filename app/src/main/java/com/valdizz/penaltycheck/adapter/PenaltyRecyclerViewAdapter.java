package com.valdizz.penaltycheck.adapter;


import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.model.entity.Penalty;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class PenaltyRecyclerViewAdapter extends RealmRecyclerViewAdapter<Penalty, PenaltyRecyclerViewAdapter.PenaltyViewHolder>{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
    private OnPenaltyClickListener penaltyClickListener;

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
        holder.penalty_card.setCardBackgroundColor(penalty.isChecked() ? Color.WHITE :holder.penalty_card.getResources().getColor(R.color.backgroundCardAlarm));

        //
        holder.penalty_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                penaltyClickListener.viewPenaltyClick(penalty);
            }
        });

        //delete penalty
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
                                penaltyClickListener.deletePenaltyClick(penalty);
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

        //pay penalty
        holder.payment_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                penaltyClickListener.payPenaltyClick(penalty);
            }
        });
    }

    class PenaltyViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.penalty_card) CardView penalty_card;
        @BindView(R.id.tv_resolutionnumber) TextView resolutionnumber;
        @BindView(R.id.tv_penaltydate) TextView penaltydate;
        @BindView(R.id.delete_button) ImageButton delete_button;
        @BindView(R.id.payment_button) ImageButton payment_button;

        PenaltyViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setPenaltyClickListener(OnPenaltyClickListener onPenaltyClickListener){
        penaltyClickListener = onPenaltyClickListener;
    }

    public interface OnPenaltyClickListener {

        void payPenaltyClick(Penalty penalty);
        void deletePenaltyClick(Penalty penalty);
        void viewPenaltyClick(Penalty penalty);
    }
}
