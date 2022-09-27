package com.octarine.plove.menu;

import android.content.Context;

//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.octarine.plove.api.models.MenuModel;
import com.octarine.plove.R;

import java.util.List;


class RecyclerMenuItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private List<Object> mItemList;
    private MenuActivity.InteractionListener mListener;
    private Context mContext;

    RecyclerMenuItemsAdapter(Context context, List<Object> itemList, MenuActivity.InteractionListener listener) {
        mItemList = itemList;
        mListener = listener;
        mContext = context;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_menu_item_new, parent, false);
            return RecyclerMenuItemViewHolder.newInstance(view);
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof RecyclerMenuItemViewHolder) {
            final MenuModel mItem = (MenuModel) getItem(position);
            final RecyclerMenuItemViewHolder holder = (RecyclerMenuItemViewHolder) viewHolder;
            holder.setTitle(mItem.displayName);
            holder.setPreview(mItem.preview);
            holder.setPrice(mItem.price + " RUB");

            holder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Log.d("IMAGE", mItem.detailImage.size3x);
            if (mItem.image.android != null) {
                Glide.with(mContext)
                        .load(mItem.image.size3x)
                        //
                        //.crossFade()
                        .centerCrop()
                        //.fitCenter()
                        .placeholder(R.drawable.big)
                        .error(R.drawable.big)
                        .into(holder.mImageView)
                        ;
            }
            holder.mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            holder.mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

            holder.card.setOnClickListener(view -> mListener.Interaction(mItem));


            holder.mBtn.setOnClickListener(view -> {
                mListener.InteractionPlus(mItem);
            });


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