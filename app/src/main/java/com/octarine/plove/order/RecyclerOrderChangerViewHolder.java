package com.octarine.plove.order;

//import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOrderChangerViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;
    public RelativeLayout card;
    public RelativeLayout mChanger;
    public TextView mQuantity;
    public ImageButton mPlus;
    public ImageButton mMinus;
    private int counter = 1;

    public RecyclerOrderChangerViewHolder(final View parent, TextView tvTitle,  RelativeLayout c, RelativeLayout changer, TextView tvQuantity, ImageButton bP, ImageButton bM) {
        super(parent);
        mTextViewTitle = tvTitle;
        card = c;
        mChanger = changer;
        mQuantity = tvQuantity;
        mPlus = bP;
        mMinus = bM;
        mQuantity.setText(String.valueOf(counter));
    }

    public static RecyclerOrderChangerViewHolder newInstance(View parent) {
        TextView tvTitle = (TextView) parent.findViewById(R.id.textViewTitle);
        RelativeLayout c = (RelativeLayout) parent.findViewById(R.id.card_menu_item);
        RelativeLayout changer = (RelativeLayout) parent.findViewById(R.id.textViewChanger);
        TextView tvQuantity = (TextView) parent.findViewById(R.id.textViewCount);
        ImageButton bPlus = (ImageButton) parent.findViewById(R.id.imageButtonPlus);
        ImageButton bMinus = (ImageButton) parent.findViewById(R.id.imageButtonMinus);
        return new RecyclerOrderChangerViewHolder(parent, tvTitle, c, changer, tvQuantity, bPlus, bMinus);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(Html.fromHtml(text));
    }
    public void setQuantity(CharSequence text) {
        mQuantity.setText(text);
    }

}