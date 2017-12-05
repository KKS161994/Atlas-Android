package com.layer.ui.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;
import android.view.View;

import com.layer.sdk.query.Queryable;
import com.layer.ui.adapters.ItemRecyclerViewAdapter;
import com.layer.ui.util.views.SwipeableItem;

public class ItemsRecyclerView<ITEM extends Queryable> extends RecyclerView {

    protected ItemRecyclerViewAdapter mAdapter;
    protected ItemTouchHelper mSwipeItemTouchHelper;

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
            scrollToTopIfNeeded(toPosition);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            scrollToTopIfNeeded(positionStart);
        }

        private void scrollToTopIfNeeded(int toPosition) {
            if (getLayoutManager() instanceof LinearLayoutManager) {
                int firstVisiblePosition = ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition();
                if (toPosition == 0 && firstVisiblePosition  < 3) {
                    scrollToPosition(toPosition);
                }
            }
        }
    };

    public ItemsRecyclerView(Context context) {
        super(context);
    }

    public ItemsRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ItemsRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void refresh() {
        if (mAdapter != null) mAdapter.refresh();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter instanceof ItemRecyclerViewAdapter) {
            //TODO This check should be removed, this is called multiple time during notifyChange
            //And it causes IllegalStateException when the same Adapter is registered with same Observer.
            if (adapter != getAdapter()) {
                super.setAdapter(adapter);
                mAdapter = (ItemRecyclerViewAdapter) adapter;
                registerAdapterDataObserver();
                refresh();
            }

        } else {
            throw new IllegalArgumentException("Adapter must be of type ItemRecyclerViewAdapter");
        }
    }

    private void registerAdapterDataObserver() {
        mAdapter.registerAdapterDataObserver(mDataObserver);
    }

    /**
     * Performs cleanup when the Activity/Fragment using the adapter is destroyed.
     */
    public void onDestroy() {
        if (mAdapter != null) {
            mAdapter.onDestroy();
            if (mAdapter.hasObservers()) {
                mAdapter.unregisterAdapterDataObserver(mDataObserver);
            }
        }
    }

    /**
     * Automatically refresh on resume
     */
    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility != View.VISIBLE) return;
        refresh();
    }

    /**
     * Convenience pass-through to this list's ItemAdapter.
     *
     * @see ItemRecyclerViewAdapter#setItemClickListener(OnItemClickListener)
     */
    public void setItemClickListener(OnItemClickListener<ITEM> listener) {
        mAdapter.setItemClickListener(listener);
    }

    public void setItemSwipeListener(SwipeableItem.OnItemSwipeListener<ITEM> listener) {
        if (mSwipeItemTouchHelper != null) {
            mSwipeItemTouchHelper.attachToRecyclerView(null);
        }
        if (listener == null) {
            mSwipeItemTouchHelper = null;
        } else {
            listener.setAdapter(mAdapter);

            mSwipeItemTouchHelper = new ItemTouchHelper(listener);
            mSwipeItemTouchHelper.attachToRecyclerView(this);
        }
    }
}
