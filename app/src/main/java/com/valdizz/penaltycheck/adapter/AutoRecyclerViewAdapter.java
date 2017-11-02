package com.valdizz.penaltycheck.adapter;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.valdizz.penaltycheck.AutoEditActivity;
import com.valdizz.penaltycheck.PenaltyActivity;
import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.db.DataHelper;
import com.valdizz.penaltycheck.model.Auto;

import java.text.SimpleDateFormat;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class AutoRecyclerViewAdapter extends RealmRecyclerViewAdapter<Auto, AutoRecyclerViewAdapter.AutoViewHolder> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");

    public AutoRecyclerViewAdapter(OrderedRealmCollection<Auto> data) {
        super(data, true);
    }

    @Override
    public AutoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_auto, parent, false);
        return new AutoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AutoViewHolder holder, final int position) {
        final Auto auto = getItem(position);
        holder.fullname.setText(auto.getSurname() + " " + auto.getName() + " " + auto.getPatronymic());
        holder.certificate.setText(holder.itemView.getContext().getResources().getString(R.string.label_certificate_short) + " " + auto.getSeries() + " " + auto.getNumber());
        holder.description.setText(auto.getDescription());
        holder.lastupdate.setText(auto.getLastupdate() != null ? holder.itemView.getContext().getResources().getString(R.string.label_lastupdate) + " " + dateFormat.format(auto.getLastupdate()) : "" + holder.itemView.getContext().getResources().getString(R.string.label_lastupdate) + " " + holder.itemView.getContext().getResources().getString(R.string.label_never));
        holder.penalties.setText(holder.itemView.getContext().getResources().getString(R.string.label_penalties) + " " + (auto.getPenalties().size() > 0 ? auto.getPenalties().size() : 0));

        holder.auto_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), PenaltyActivity.class).putExtra(DataHelper.AUTOID_PARAM, auto.getId()));
            }
        });
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(android.R.string.dialog_alert_title)
                        .setMessage(R.string.dialog_deleteauto)
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DataHelper.deleteAuto(auto.getId());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });
        holder.edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), AutoEditActivity.class).putExtra(DataHelper.AUTOID_PARAM, auto.getId()));
            }
        });
        holder.reload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check penalty
            }
        });
    }

    class AutoViewHolder extends RecyclerView.ViewHolder {

        CardView auto_card;
        TextView fullname, certificate, description, lastupdate, penalties;
        ImageButton delete_button, edit_button, reload_button;
        ImageView auto_image;

        AutoViewHolder(View view) {
            super(view);
            auto_card = (CardView)view.findViewById(R.id.auto_card);
            delete_button = (ImageButton) view.findViewById(R.id.delete_button);
            edit_button = (ImageButton) view.findViewById(R.id.edit_button);
            reload_button = (ImageButton) view.findViewById(R.id.reload_button);
            fullname = (TextView) view.findViewById(R.id.tv_fullname);
            certificate = (TextView) view.findViewById(R.id.tv_certificate);
            description = (TextView) view.findViewById(R.id.tv_description);
            lastupdate = (TextView) view.findViewById(R.id.tv_lastupdate);
            penalties = (TextView) view.findViewById(R.id.tv_penalties);
            auto_image = (ImageView) view.findViewById(R.id.auto_image);
        }
    }


}
