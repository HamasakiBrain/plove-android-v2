package com.octarine.plove.order;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOrderStepKeyValueItemViewHolder extends RecyclerView.ViewHolder {
    public  TextView mTextViewTitle;
    public  TextView mTextViewValue;
    public RelativeLayout mBackground;


    public RecyclerOrderStepKeyValueItemViewHolder(final View parent, TextView title, TextView value, RelativeLayout background) {
        super(parent);
        mTextViewTitle = title;
        mTextViewValue = value;
        mBackground = background;
    }

    public static RecyclerOrderStepKeyValueItemViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.textViewItem);
        TextView value = (TextView) parent.findViewById(R.id.textViewItemValue);
        RelativeLayout background = (RelativeLayout) parent.findViewById(R.id.rl1);
        return new RecyclerOrderStepKeyValueItemViewHolder(parent, title, value, background);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }
    public void setValue(String text) {
        mTextViewValue.setText(text);
    }



}