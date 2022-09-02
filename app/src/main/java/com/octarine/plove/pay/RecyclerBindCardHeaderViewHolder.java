package com.octarine.plove.pay;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerBindCardHeaderViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;


    public RecyclerBindCardHeaderViewHolder(final View parent, TextView title) {
        super(parent);
        mTextViewTitle = title;

    }

    public static RecyclerBindCardHeaderViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.textViewHeader);
        return new RecyclerBindCardHeaderViewHolder(parent, title);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }



}