package com.octarine.plove.order;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerOrderStepItemEditableViewHolder extends RecyclerView.ViewHolder {
    public EditText mTextViewTitle;
    public TextView mTextViewAlert;
    public RelativeLayout mBackground;
    public ImageButton mCheckbox;
    public int mParamCode;


    public RecyclerOrderStepItemEditableViewHolder(final View parent, EditText title, RelativeLayout background, ImageButton checkbox, TextView alert) {
        super(parent);
        mTextViewTitle = title;
        mBackground = background;
        mCheckbox = checkbox;
        mTextViewAlert = alert;
        //mTextViewTitle.setText("Test");
    }

    public static RecyclerOrderStepItemEditableViewHolder newInstance(View parent) {
        EditText title = (EditText) parent.findViewById(R.id.editTextTitle);
        TextView alert = (TextView) parent.findViewById(R.id.textViewAlert);
        RelativeLayout background = (RelativeLayout) parent.findViewById(R.id.rl1);
        ImageButton checkbox = (ImageButton) parent.findViewById(R.id.imageButtonCheck);
        return new RecyclerOrderStepItemEditableViewHolder(parent, title, background, checkbox, alert);
    }

    public void setTitle(String text) {
        mTextViewTitle.setHint(text);
    }

    public void setChecked() {
        mCheckbox.setVisibility(View.VISIBLE);
    }
    public void setUnChecked() {
        mCheckbox.setVisibility(View.GONE);
    }

    public void setAlert(boolean alert, Context context) {
        if (alert) {
            mTextViewAlert.setVisibility(View.VISIBLE);
            mTextViewTitle.setBackgroundResource(R.drawable.input_error_bg);
            mTextViewTitle.setHintTextColor(context.getResources().getColor(R.color.plove_red));
            mTextViewTitle.setTextColor(context.getResources().getColor(R.color.plove_red));
        } else {
            mTextViewAlert.setVisibility(View.GONE);
            mTextViewTitle.setBackgroundResource(R.drawable.input_normal_bg);
            mTextViewTitle.setHintTextColor(context.getResources().getColor(R.color.order_item_placeholder));
            mTextViewTitle.setTextColor(context.getResources().getColor(R.color.order_item_text));
        }
    }

    public void setmParamCode(final int paramCode, final Context context) {
        mParamCode = paramCode;

    }

    public void setValue(String val) {
        this.mTextViewTitle.setText(val);
    }


}