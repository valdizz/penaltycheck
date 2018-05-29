package com.valdizz.penaltycheck.adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.valdizz.penaltycheck.R;
import com.valdizz.penaltycheck.model.entity.Auto;
import com.valdizz.penaltycheck.model.entity.Penalty;
import com.valdizz.penaltycheck.util.ImageUtils;

import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class AutoRecyclerViewAdapter extends RealmRecyclerViewAdapter<Auto, AutoRecyclerViewAdapter.AutoViewHolder>  implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener{

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy  HH:mm");
    private OnAutoClickListener autoClickListener;

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
        holder.fullname.setText(auto.getFullName());
        holder.certificate.setText(holder.itemView.getContext().getResources().getString(R.string.label_certificate_short) + " " + auto.getSeries() + " " + auto.getNumber());
        holder.description.setText(auto.getDescription());
        holder.lastupdate.setText(auto.getLastupdate() != null ? holder.itemView.getContext().getResources().getString(R.string.label_lastupdate) + " " + dateFormat.format(auto.getLastupdate()) : "" + holder.itemView.getContext().getResources().getString(R.string.label_lastupdate) + " " + holder.itemView.getContext().getResources().getString(R.string.label_never));
        if (getNewPenalties(auto.getPenalties()) > 0) {
            holder.penalties.setText(holder.itemView.getContext().getResources().getString(R.string.label_penalties) + " " + (auto.getPenalties().size()) + " / " + holder.itemView.getContext().getResources().getString(R.string.label_new_penalties) + " " + getNewPenalties(auto.getPenalties()));
            holder.penalties.setTextColor(Color.RED);
        }
        else {
            holder.penalties.setText(holder.itemView.getContext().getResources().getString(R.string.label_penalties) + " " + (auto.getPenalties().size() > 0 ? auto.getPenalties().size() : 0));
            holder.penalties.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.textGray));
        }
        if (auto.getImage().length > 0) {
            RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(holder.itemView.getContext().getResources(), ImageUtils.convertBytesToImage(auto.getImage()));
            dr.setCornerRadius(25);
            holder.auto_image.setImageDrawable(dr);
        }
        else {
            holder.auto_image.setImageDrawable(ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.empty_car));
        }

        //get penalties for this auto
        holder.auto_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                autoClickListener.getPenaltiesClick(auto.getId());
            }
        });
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int position, int direction) {
        final Auto auto = getItem(position);
        //delete auto
        if (direction == ItemTouchHelper.LEFT) {
            new AlertDialog.Builder(viewHolder.itemView.getContext())
                    .setTitle(android.R.string.dialog_alert_title)
                    .setMessage(R.string.dialog_deleteauto)
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            autoClickListener.deleteAutoClick(auto.getId());
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            notifyItemChanged(position);
                            dialogInterface.dismiss();
                        }
                    })
                    .show();
        }
        //edit auto
        else {
            autoClickListener.editAutoClick(auto.getId());
            notifyItemChanged(position);
        }
    }

    private int getNewPenalties(List<Penalty> penalties){
        int sum = 0;
        for (Penalty penalty : penalties){
            if (!penalty.isChecked()){
                sum++;
            }
        }
        return sum;
    }

    class AutoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.auto_card) CardView auto_card;
        @BindView(R.id.tv_fullname) TextView fullname;
        @BindView(R.id.tv_certificate) TextView certificate;
        @BindView(R.id.tv_description) TextView description;
        @BindView(R.id.tv_lastupdate) TextView lastupdate;
        @BindView(R.id.tv_penalties) TextView penalties;
        @BindView(R.id.auto_image) ImageView auto_image;

        AutoViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setAutoClickListener(OnAutoClickListener onAutoClickListener){
        autoClickListener = onAutoClickListener;
    }

    public interface OnAutoClickListener {
        void editAutoClick(long id);
        void deleteAutoClick(long id);
        void getPenaltiesClick(long id);
    }

}
