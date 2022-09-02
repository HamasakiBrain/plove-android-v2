package com.octarine.plove.history;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class HistoryChildItemViewHolder extends RecyclerView.ViewHolder {

        TextView mTitle;
        TextView mCount;
        TextView mAmount;

    private HistoryChildItemViewHolder(final View parent,
        TextView title,
        TextView count,
        TextView amount
    ) {
            super(parent);
            this.mTitle = title;
            this.mCount = count;
            this.mAmount = amount;
        }

    public static HistoryChildItemViewHolder newInstance(View parent) {
        return new HistoryChildItemViewHolder(parent,
                parent.findViewById(R.id.textViewHistoryPositionName),
                parent.findViewById(R.id.textViewHistoryPositionCount),
                parent.findViewById(R.id.textViewHistoryPositionPrice));
    }

}
