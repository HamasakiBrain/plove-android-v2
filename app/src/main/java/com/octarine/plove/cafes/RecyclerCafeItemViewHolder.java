package com.octarine.plove.cafes;

//import android.support.v7.widget.CardView;
//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerCafeItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitleBlack;
    public final TextView mTextViewTitlePreview;
    public final ImageView mImageViewPreview;
    public CardView card;

    public RecyclerCafeItemViewHolder(final View parent, TextView textViewTitleBlack, TextView textViewTitlePreview, ImageView imageViewPreview, CardView card) {
        super(parent);
        this.mTextViewTitleBlack = textViewTitleBlack;
        this.mTextViewTitlePreview = textViewTitlePreview;
        this.mImageViewPreview = imageViewPreview;
        this.card = card;
    }

    public static RecyclerCafeItemViewHolder newInstance(View parent) {
        TextView mTextViewTitleBlack = (TextView) parent.findViewById(R.id.textViewSaleTitle1);
        TextView mTextViewTitlePreview = (TextView) parent.findViewById(R.id.textViewSalePreview);
        ImageView mImageViewPreview = (ImageView) parent.findViewById(R.id.imageViewSale);
        CardView mCard = (CardView) parent.findViewById(R.id.card_cafe);
        RecyclerCafeItemViewHolder view =  new RecyclerCafeItemViewHolder(parent, mTextViewTitleBlack, mTextViewTitlePreview, mImageViewPreview, mCard);
        return view;
    }

    public void setTextViewTitleBlack(CharSequence text) {
        mTextViewTitleBlack.setText(text);
    }

    public void setTextViewTitlePreview(CharSequence text) {
        mTextViewTitlePreview.setText(text);
    }

}