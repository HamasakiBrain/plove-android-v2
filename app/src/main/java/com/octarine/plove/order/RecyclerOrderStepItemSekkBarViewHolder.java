package com.octarine.plove.order;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOrderStepItemSekkBarViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextViewTitle;
    public TextView mTextViewValue;
    public RelativeLayout mBackground;
    public SeekBar mBar;


    public RecyclerOrderStepItemSekkBarViewHolder(final View parent, TextView title, TextView value, RelativeLayout background, SeekBar bar) {
        super(parent);
        mTextViewTitle = title;
        mTextViewValue = value;
        mBackground = background;
        mBar = bar;
    }

    public static RecyclerOrderStepItemSekkBarViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.textViewItem);
        TextView value = (TextView) parent.findViewById(R.id.textViewItemValue);
        RelativeLayout background = (RelativeLayout) parent.findViewById(R.id.rl1);
        SeekBar bar = (SeekBar) parent.findViewById(R.id.seekBar);
        return new RecyclerOrderStepItemSekkBarViewHolder(parent, title, value, background, bar);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }
    public void setValue(String text) {
        mTextViewValue.setText(text);
    }
    public void setBarMax(int max) {
        mBar.setMax(max);
    }


}