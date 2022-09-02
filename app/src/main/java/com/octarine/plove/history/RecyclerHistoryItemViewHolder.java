package com.octarine.plove.history;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;
import com.octarine.plove.api.models.HistoryResponseModel;

public class RecyclerHistoryItemViewHolder extends RecyclerView.ViewHolder {
    private TextView mWhere;
    private TextView mDate;
    private TextView mPlus;
    private TextView mMinus;
    private TextView mTotal;
    private RecyclerView mPositions;

    private RecyclerHistoryItemViewHolder(final View parent, TextView where, TextView date, TextView plus, TextView minus, TextView total, RecyclerView positions) {
        super(parent);
        mWhere = where;
        mDate = date;
        mPlus = plus;
        mMinus = minus;
        mTotal = total;
        mPositions = positions;
    }

    public static RecyclerHistoryItemViewHolder newInstance(View parent) {
        TextView whereTextView = parent.findViewById(R.id.textViewWhere);
        TextView dateTextView = parent.findViewById(R.id.textViewDate);
        TextView plusTextView = parent.findViewById(R.id.textViewPlus);
        TextView minusTextView = parent.findViewById(R.id.textViewMinus);
        TextView totalTextView = parent.findViewById(R.id.textViewTotal);
        RecyclerView positions = parent.findViewById(R.id.recycler_view_history_positions);
        return new RecyclerHistoryItemViewHolder(parent, whereTextView, dateTextView, plusTextView, minusTextView, totalTextView, positions);
    }

    void bind(HistoryResponseModel.History item) {
        mWhere.setText(item.where);
        mDate.setText(item.datetime);
        mPlus.setText("+"+item.pointsPlus+" бал.");
        if (Float.valueOf(item.pointsPlus) == 0) mPlus.setVisibility(View.GONE); else mPlus.setVisibility(View.VISIBLE);
        mMinus.setText(item.pointsMinus+" бал.");
        if (Float.valueOf(item.pointsMinus) == 0) mMinus.setVisibility(View.GONE); else mMinus.setVisibility(View.VISIBLE);
        mTotal.setText(item.total + " руб.");
        RecyclerView.LayoutManager layoutManager = new CustomLinearLayoutManager(itemView.getContext());
        mPositions.setLayoutManager(layoutManager);
        HistoryChildRecyclerViewAdapter adapter = new HistoryChildRecyclerViewAdapter(itemView.getContext(), item.positions);
        mPositions.setAdapter(adapter);
    }

}