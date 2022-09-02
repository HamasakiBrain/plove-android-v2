package com.octarine.plove.settings;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOptionsHeaderViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;


    public RecyclerOptionsHeaderViewHolder(final View parent, TextView title) {
        super(parent);
        mTextViewTitle = title;

    }

    public static RecyclerOptionsHeaderViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.textViewHeader);
        return new RecyclerOptionsHeaderViewHolder(parent, title);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }



}