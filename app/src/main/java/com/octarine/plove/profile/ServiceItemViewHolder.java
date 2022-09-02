package com.octarine.plove.profile;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class ServiceItemViewHolder extends RecyclerView.ViewHolder {

    public TextView mTitle;
    public ImageView mImage;
    public CardView mCard;

    private ServiceItemViewHolder(final View parent, TextView title, ImageView image, CardView card) {
        super(parent);
        this.mTitle = title;
        this.mImage = image;
        this.mCard = card;
    }

    public static ServiceItemViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.title);
        CardView card = (CardView) parent.findViewById(R.id.card_view);
        ImageView image = (ImageView) parent.findViewById(R.id.imageView);
        return new ServiceItemViewHolder(parent, title, image, card);
    }


}