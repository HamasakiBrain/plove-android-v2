package com.octarine.plove.profile;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.octarine.plove.R;

public class ProfileHeaderViewHolder extends RecyclerView.ViewHolder {

    public RecyclerView recyclerView;
    public TextView mBalance;
    public FloatingActionButton mScanner;

    private ProfileHeaderViewHolder(final View parent, RecyclerView recyclerView,TextView balance, FloatingActionButton scanner) {
        super(parent);
        this.recyclerView = recyclerView;
        this.mBalance = balance;
        this.mScanner = scanner;
    }

    public static ProfileHeaderViewHolder newInstance(View parent) {
        RecyclerView recyclerView = parent.findViewById(R.id.recyclerView);
        TextView balance = (TextView) parent.findViewById(R.id.bal_val);
        FloatingActionButton scanner = (FloatingActionButton) parent.findViewById(R.id.floatingActionButton);
        return new ProfileHeaderViewHolder(parent, recyclerView, balance, scanner);
    }


}