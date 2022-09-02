package com.octarine.plove.bag;

//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerBagItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;
    public final ImageView mImageView;
    public RelativeLayout card;
    public RelativeLayout mChanger;
    public TextView mQuantity;
    public TextView mPrice;
    public ImageButton mPlus;
    public ImageButton mMinus;

    public RecyclerBagItemViewHolder(final View parent, TextView tvTitle, ImageView iv, RelativeLayout c, RelativeLayout changer, TextView tvQuantity, ImageButton bP, ImageButton bM, TextView p) {
        super(parent);
        mTextViewTitle = tvTitle;
        mImageView = iv;
        card = c;
        mChanger = changer;
        mQuantity = tvQuantity;
        mPlus = bP;
        mMinus = bM;
        mPrice = p;

    }

    public static RecyclerBagItemViewHolder newInstance(View parent) {
        TextView tvTitle = (TextView) parent.findViewById(R.id.textViewMenuItemTitle);
        ImageView iv = (ImageView) parent.findViewById(R.id.imageViewMenuItemImg);
        RelativeLayout c = (RelativeLayout) parent.findViewById(R.id.card_menu_item);
        RelativeLayout changer = (RelativeLayout) parent.findViewById(R.id.textViewMenuItemChanger);
        TextView tvQuantity = (TextView) parent.findViewById(R.id.textViewCount);
        TextView p = (TextView) parent.findViewById(R.id.textViewMenuItemPrice);
        ImageButton bPlus = (ImageButton) parent.findViewById(R.id.imageButtonPlus);
        ImageButton bMinus = (ImageButton) parent.findViewById(R.id.imageButtonMinus);
        return new RecyclerBagItemViewHolder(parent, tvTitle, iv, c, changer, tvQuantity, bPlus, bMinus,p);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(Html.fromHtml(text));
    }
    public void setQuantity(CharSequence text) {
        mQuantity.setText(text);
    }

}