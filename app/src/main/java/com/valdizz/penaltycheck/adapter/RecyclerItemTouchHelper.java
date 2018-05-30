package com.valdizz.penaltycheck.adapter;

import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.valdizz.penaltycheck.R;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;

    public RecyclerItemTouchHelper(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            listener.onSwiped(viewHolder, viewHolder.getAdapterPosition(), direction == ItemTouchHelper.LEFT ? ItemTouchHelper.LEFT : ItemTouchHelper.RIGHT);
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            View itemView = viewHolder.itemView;
            int height = itemView.getBottom() - itemView.getTop();
            int width = height / 3;
            //swipe right - edit
            if (dX > 0){
                Paint paint = new Paint();
                paint.setColor(itemView.getResources().getColor(R.color.darkGreenButton));
                RectF background  = new RectF(itemView.getLeft(), itemView.getTop(), dX, itemView.getBottom());
                c.drawRect(background , paint);
                RectF icon_dest = new RectF(itemView.getLeft() + width, itemView.getTop() + width, itemView.getLeft() + 2 * width, itemView.getBottom() - width);
                c.drawBitmap(BitmapFactory.decodeResource(itemView.getResources(), R.drawable.ic_edit), null, icon_dest, paint);
            }
            //swipe left - delete
            else {
                Paint paint = new Paint();
                paint.setColor(Color.RED);
                RectF background  = new RectF(itemView.getRight()+dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());
                c.drawRect(background , paint);
                RectF icon_dest = new RectF(itemView.getRight() - 2 * width, itemView.getTop() + width, itemView.getRight() - width, itemView.getBottom() - width);
                c.drawBitmap(BitmapFactory.decodeResource(itemView.getResources(), R.drawable.ic_delete), null, icon_dest, paint);
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface RecyclerItemTouchHelperListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int position, int direction);
    }
}
