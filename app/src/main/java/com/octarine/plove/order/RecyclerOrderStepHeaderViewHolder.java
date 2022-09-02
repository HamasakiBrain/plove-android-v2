package com.octarine.plove.order;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOrderStepHeaderViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;


    public RecyclerOrderStepHeaderViewHolder(final View parent, TextView title) {
        super(parent);
        mTextViewTitle = title;

    }

    public static RecyclerOrderStepHeaderViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.textViewHeader);
        return new RecyclerOrderStepHeaderViewHolder(parent, title);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }



}