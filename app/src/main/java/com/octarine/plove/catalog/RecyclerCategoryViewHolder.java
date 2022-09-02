package com.octarine.plove.catalog;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerCategoryViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;
    public final ImageView mImageView;
    public RelativeLayout mCard;

    public RecyclerCategoryViewHolder(final View parent, TextView tvTitle, ImageView iv, RelativeLayout card) {
        super(parent);
        mTextViewTitle = tvTitle;
        mImageView = iv;
        mCard = card;
    }

    public static RecyclerCategoryViewHolder newInstance(View parent) {
        TextView tvTitle = (TextView) parent.findViewById(R.id.textViewMenuItemTitle);
        ImageView iv = (ImageView) parent.findViewById(R.id.imageViewMenuItemImg);
        RelativeLayout card = (RelativeLayout) parent.findViewById(R.id.card_category);
        return new RecyclerCategoryViewHolder(parent, tvTitle, iv, card);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }


}