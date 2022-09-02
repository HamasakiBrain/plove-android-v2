package com.octarine.plove.settings;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOptionsItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;
    public RelativeLayout mBackground;


    public RecyclerOptionsItemViewHolder(final View parent, TextView title, RelativeLayout background) {
        super(parent);
        mTextViewTitle = title;
        mBackground = background;
    }

    public static RecyclerOptionsItemViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.textViewItem);
        RelativeLayout background = (RelativeLayout) parent.findViewById(R.id.rl1);
        return new RecyclerOptionsItemViewHolder(parent, title, background);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }




}