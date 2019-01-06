package com.xuh.shopping.tools;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.xuh.shopping.Index_message;

public class ItemTouchHelperCallBack extends ItemTouchHelper.Callback {
    private MessageRecyclerViewAdapter messageRecyclerViewAdapter;
    private CartRecyclerViewAdapter cartRecyclerViewAdapter;
    public ItemTouchHelperCallBack(MessageRecyclerViewAdapter messageRecyclerViewAdapter){
        this.messageRecyclerViewAdapter = messageRecyclerViewAdapter;
    }
    public ItemTouchHelperCallBack(CartRecyclerViewAdapter cartRecyclerViewAdapter){
        this.cartRecyclerViewAdapter = cartRecyclerViewAdapter;
    }
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.RIGHT;
        return makeMovementFlags( dragFlags,swipeFlags );
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

        if (messageRecyclerViewAdapter != null)
            messageRecyclerViewAdapter.onItemMove( viewHolder.getAdapterPosition(),target.getAdapterPosition() );
        if (cartRecyclerViewAdapter != null)
            cartRecyclerViewAdapter.onItemMove( viewHolder.getAdapterPosition(),target.getAdapterPosition() );
        return true;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw( c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive );
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            final float alpha = 1 - Math.abs( dX )/(float)viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha( alpha );
        }
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (messageRecyclerViewAdapter != null)
            messageRecyclerViewAdapter.OnItemDelete(viewHolder.getAdapterPosition());
        if (cartRecyclerViewAdapter != null)
            cartRecyclerViewAdapter.OnItemDelete(viewHolder.getAdapterPosition());
    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return true;
    }
}
