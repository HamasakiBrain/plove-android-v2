package com.octarine.plove.pay;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerBindCardItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;
    public RelativeLayout mBackground;
    public ImageView mImage;


    public RecyclerBindCardItemViewHolder(final View parent, TextView title, RelativeLayout background, ImageView image) {
        super(parent);
        mTextViewTitle = title;
        mBackground = background;
        mImage = image;
    }

    public static RecyclerBindCardItemViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.textViewItem);
        RelativeLayout background = (RelativeLayout) parent.findViewById(R.id.rl1);
        ImageView image = (ImageView) parent.findViewById(R.id.imageViewCard);
        return new RecyclerBindCardItemViewHolder(parent, title, background, image);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }
    public void setImage(Drawable drawable) {
        if (drawable!=null){
            mImage.setImageDrawable(drawable);
            mImage.setVisibility(View.VISIBLE);
        } else {
            mImage.setVisibility(View.INVISIBLE);
        }
    }



}