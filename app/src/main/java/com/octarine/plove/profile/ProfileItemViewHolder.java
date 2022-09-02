package com.octarine.plove.profile;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class ProfileItemViewHolder extends RecyclerView.ViewHolder {

    public  TextView mTitle;
    public  TextView mDesc;
    public ImageView mImage;
    public RelativeLayout container;

    private ProfileItemViewHolder(final View parent, RelativeLayout container, TextView t, TextView d, ImageView i) {
        super(parent);
        this.container = container;
        this.mTitle = t;
        this.mDesc = d;
        this.mImage = i;
    }

    public static ProfileItemViewHolder newInstance(View parent) {
        TextView t = (TextView) parent.findViewById(R.id.s1);
        TextView d = (TextView) parent.findViewById(R.id.s2);
        ImageView i = (ImageView) parent.findViewById(R.id.imageViewSale);
        RelativeLayout container = parent.findViewById(R.id.container);
        return new ProfileItemViewHolder(parent, container, t, d, i);
    }


}