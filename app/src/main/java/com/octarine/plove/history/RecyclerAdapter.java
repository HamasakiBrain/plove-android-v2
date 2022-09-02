package com.octarine.plove.history;

//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mItemList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public RecyclerAdapter(List<String> itemList) {
        mItemList = itemList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
            return RecyclerItemViewHolder.newInstance(view);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_header, parent, false);
            return RecyclerHeaderViewHolder.newInstance(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {



        if (viewHolder instanceof RecyclerItemViewHolder) {
            RecyclerItemViewHolder holder = (RecyclerItemViewHolder) viewHolder;
            String itemText = mItemList.get(position);
            holder.setItemText(itemText);
        } else if (viewHolder instanceof RecyclerHeaderViewHolder) {
            //cast holder to VHHeader and set data for header.
        }

    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
    }

}