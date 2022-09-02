package com.octarine.plove.order;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOrderStepItemViewHolder extends RecyclerView.ViewHolder {
    public final TextView mTextViewTitle;
    public RelativeLayout mBackground;
    public ImageButton mCheckbox;


    public RecyclerOrderStepItemViewHolder(final View parent, TextView title, RelativeLayout background, ImageButton checkbox) {
        super(parent);
        mTextViewTitle = title;
        mBackground = background;
        mCheckbox = checkbox;
    }

    public static RecyclerOrderStepItemViewHolder newInstance(View parent) {
        TextView title = (TextView) parent.findViewById(R.id.textViewItem);
        RelativeLayout background = (RelativeLayout) parent.findViewById(R.id.rl1);
        ImageButton checkbox = (ImageButton) parent.findViewById(R.id.imageButtonCheck);
        return new RecyclerOrderStepItemViewHolder(parent, title, background, checkbox);
    }

    public void setTitle(String text) {
        mTextViewTitle.setText(text);
    }
    public void setChecked() {
        mCheckbox.setVisibility(View.VISIBLE);
        mBackground.setBackgroundResource(R.color.order_selected_item);
    }
    public void setUnChecked() {
        mCheckbox.setVisibility(View.INVISIBLE);
        mBackground.setBackgroundResource(R.color.white);
    }


}