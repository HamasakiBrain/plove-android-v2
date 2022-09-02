package com.octarine.plove.menu;

//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerMenuItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;
    public final TextView mTextViewPreview;
    public final TextView mTextViewPrice;
    public final ImageView mImageView;
    public RelativeLayout card;
    public Button mBtn;

    public RecyclerMenuItemViewHolder(final View parent, TextView tvTitle, TextView tvPrice, TextView tvPreview, ImageView iv, RelativeLayout c
            , Button b) {
        super(parent);
        mTextViewTitle = tvTitle;
        mTextViewPreview = tvPreview;
        mTextViewPrice = tvPrice;
        mImageView = iv;
        card = c;
        mBtn = b;

    }

    public static RecyclerMenuItemViewHolder newInstance(View parent) {
        TextView tvPrice = (TextView) parent.findViewById(R.id.textViewMenuItemPrice);
        TextView tvTitle = (TextView) parent.findViewById(R.id.textViewMenuItemTitle);
        TextView tvPreview = (TextView) parent.findViewById(R.id.textViewMenuItemPreview);
        ImageView iv = (ImageView) parent.findViewById(R.id.imageViewMenuItemImg);
        RelativeLayout c = (RelativeLayout) parent.findViewById(R.id.card_menu_item);
        Button b = (Button) parent.findViewById(R.id.buyBtn);
        return new RecyclerMenuItemViewHolder(parent, tvTitle, tvPrice, tvPreview, iv, c, b);
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