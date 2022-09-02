package com.octarine.plove.menu;

//import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

public class RecyclerLoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;


    public RecyclerLoadingViewHolder(View parent) {
        super(parent);
        progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar1);
    }

}