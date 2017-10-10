package com.valdizz.penaltycheck.adapter;


import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.valdizz.penaltycheck.AutoEditActivity;
import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.db.DataHelper;
import com.valdizz.penaltycheck.model.Auto;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class AutoRecyclerViewAdapter extends RealmRecyclerViewAdapter<Auto, AutoRecyclerViewAdapter.AutoViewHolder> {

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
        holder.fullname.setText(auto.getSurname()+" "+auto.getName()+" "+auto.getPatronymic());
        holder.certificate.setText(holder.itemView.getContext().getResources().getString(R.string.label_certificate_short)+" "+auto.getSeries()+" "+auto.getNumber());
        holder.description.setText(auto.getDescription()+" / "+auto.getId());
        holder.lastupdate.setText(auto.getLastupdate()!=null ? holder.itemView.getContext().getResources().getString(R.string.label_lastupdate)+auto.getLastupdate().toString() : " "+holder.itemView.getContext().getResources().getString(R.string.label_lastupdate)+" "+holder.itemView.getContext().getResources().getString(R.string.label_never));
        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        holder.item_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(view.getContext())
                        .setTitle(android.R.string.dialog_alert_title)
                        .setMessage(R.string.dialog_deleteauto)
                        .setCancelable(true)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DataHelper.deleteAuto(auto.getId());
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
        holder.item_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getContext().startActivity(new Intent(view.getContext(), AutoEditActivity.class).putExtra("auto_id", auto.getId()));
            }
        });
    }

    class AutoViewHolder extends RecyclerView.ViewHolder{
        SwipeLayout swipeLayout;
        TextView fullname;
        TextView certificate;
        TextView description;
        TextView lastupdate;
        ImageView item_delete, item_edit;

        AutoViewHolder(View view){
            super(view);
            swipeLayout = (SwipeLayout) view.findViewById(R.id.swipe_auto);
            item_delete = (ImageView) view.findViewById(R.id.item_auto_delete);
            item_edit = (ImageView) view.findViewById(R.id.item_auto_edit);
            fullname = (TextView) view.findViewById(R.id.tvFullname);
            certificate = (TextView) view.findViewById(R.id.tvCertificate);
            description = (TextView) view.findViewById(R.id.tvDescription);
            lastupdate = (TextView) view.findViewById(R.id.tvLastupdate);
        }
    }


}
