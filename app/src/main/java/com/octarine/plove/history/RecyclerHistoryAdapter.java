package com.octarine.plove.history;

//import android.content.Context;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//import com.gun0912.tedpicker.custom.adapter.BaseRecyclerViewAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.api.models.HistoryResponseModel;
//import com.octarine.plove.common.OnLoadMoreListener;
import com.octarine.plove.R;
//import com.octarine.plove.menu.RecyclerLoadingViewHolder;

import java.util.List;


public class RecyclerHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HistoryResponseModel.History> mItems;
    //private static final int TYPE_HISTORY_HEADER = 0;
    private static final int TYPE_HISTORY_ITEM = 1;

    RecyclerHistoryAdapter(List<HistoryResponseModel.History> items) {
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_history_item, parent, false);
        return RecyclerHistoryItemViewHolder.newInstance(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof RecyclerHistoryItemViewHolder) {
            RecyclerHistoryItemViewHolder holder = (RecyclerHistoryItemViewHolder) viewHolder;
            HistoryResponseModel.History model = mItems.get(position);
            holder.bind(model);
            return;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mItems.get(position) instanceof HistoryResponseModel.History)
            return TYPE_HISTORY_ITEM;
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


}