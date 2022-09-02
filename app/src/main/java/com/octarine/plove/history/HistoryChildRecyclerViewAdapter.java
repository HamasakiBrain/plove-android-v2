package com.octarine.plove.history;

/**
 * Created by rustam on 26.04.2018.
 */

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;
import com.octarine.plove.api.models.HistoryResponseModel;

import java.util.List;

public class HistoryChildRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<HistoryResponseModel.Position> contents;
    private Context mContext;

    static final int TYPE_HEADER = 0;
    static final int TYPE_CELL = 1;

    HistoryChildRecyclerViewAdapter(Context context, List<HistoryResponseModel.Position> contents) {
        this.contents = contents;
        this.mContext = context;
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_CELL;
    }

    @Override
    public int getItemCount() {
        return contents.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_history_position, parent, false);
        return HistoryChildItemViewHolder.newInstance(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HistoryChildItemViewHolder viewHolder = (HistoryChildItemViewHolder) holder;
        viewHolder.mTitle.setText(contents.get(viewHolder.getAdapterPosition()).displayName);
        viewHolder.mCount.setText(contents.get(viewHolder.getAdapterPosition()).count);
        viewHolder.mAmount.setText(contents.get(viewHolder.getAdapterPosition()).totalPrice + " руб.");
    }
}
