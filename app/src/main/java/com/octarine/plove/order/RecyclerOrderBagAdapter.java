package com.octarine.plove.order;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.octarine.plove.R;
import com.octarine.plove.api.models.BagItem;
import com.octarine.plove.menu.RecyclerLoadingViewHolder;

import java.util.List;


public class RecyclerOrderBagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context mContext;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Object> mItemList;

    public RecyclerOrderBagAdapter(Context context, List<Object> itemList) {
        mItemList = itemList;
        mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_order_bag_item, parent, false);
            return RecyclerOrderBagItemViewHolder.newInstance(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_load, parent, false);
            return new RecyclerLoadingViewHolder(view);
        }
        return null;
    }


    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof RecyclerOrderBagItemViewHolder) {
            // get the article
            final BagItem mItem = (BagItem) getItem(position);
            final RecyclerOrderBagItemViewHolder holder = (RecyclerOrderBagItemViewHolder) viewHolder;

            holder.setTitle(mItem.displayName);
            holder.setPrice(String.valueOf(mItem.count)+" x "+mItem.price);
            holder.setPreview(mItem.preview);

            if (mItem.image != null) {
                Glide.with(mContext)
                        .load(mItem.image.replace("https", "http"))
                        .asBitmap()
                        .fitCenter()
                        .placeholder(R.drawable.food_menu_placeholder)
                        .into(holder.mImageView);
            }

        } else {
            if (viewHolder instanceof RecyclerLoadingViewHolder) {
                RecyclerLoadingViewHolder loadingViewHolder = (RecyclerLoadingViewHolder) viewHolder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }


    }
    private Object getItem(int i) {
        return mItemList.get(i);
    }

    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

}