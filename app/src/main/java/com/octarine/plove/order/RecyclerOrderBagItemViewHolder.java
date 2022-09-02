package com.octarine.plove.order;

//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOrderBagItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;
    public final TextView mTextViewPreview;
    public final TextView mTextViewPrice;
    public final ImageView mImageView;

    public RecyclerOrderBagItemViewHolder(final View parent, TextView tvTitle, TextView tvPrice, TextView tvPreview, ImageView iv) {
        super(parent);
        mTextViewTitle = tvTitle;
        mTextViewPreview = tvPreview;
        mTextViewPrice = tvPrice;
        mImageView = iv;


    }

    public static RecyclerOrderBagItemViewHolder newInstance(View parent) {
        TextView tvPrice = (TextView) parent.findViewById(R.id.textViewMenuItemPrice);
        TextView tvTitle = (TextView) parent.findViewById(R.id.textViewMenuItemTitle);
        TextView tvPreview = (TextView) parent.findViewById(R.id.textViewMenuItemPreview);
        ImageView iv = (ImageView) parent.findViewById(R.id.imageViewMenuItemImg);
        return new RecyclerOrderBagItemViewHolder(parent, tvTitle, tvPrice, tvPreview, iv);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(Html.fromHtml(text));
    }
    public void setPreview(CharSequence text) {
        mTextViewPreview.setText(text);
    }
    public void setPrice(CharSequence text) {
        mTextViewPrice.setText(text);
    }


}