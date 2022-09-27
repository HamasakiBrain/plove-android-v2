package com.octarine.plove.bag;

import android.content.Context;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.octarine.plove.api.models.BagItem;
import com.octarine.plove.R;
import com.octarine.plove.menu.RecyclerLoadingViewHolder;

import java.util.List;


public class RecyclerBagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    int mShortAnimationDuration;
    private List<Object> mItemList;
    private BagActivity.InteractionListener mListener;
    private Context mContext;

    public RecyclerBagAdapter(Context context, List<Object> itemList, BagActivity.InteractionListener listener) {
        mItemList = itemList;
        mListener = listener;
        mContext = context;
        mShortAnimationDuration = context.getResources().getInteger(
                android.R.integer.config_shortAnimTime);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_bag_item, parent, false);
            return RecyclerBagItemViewHolder.newInstance(view);
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

        if (viewHolder instanceof RecyclerBagItemViewHolder) {

            BagItem mItem = (BagItem) getItem(position);
            final RecyclerBagItemViewHolder holder = (RecyclerBagItemViewHolder) viewHolder;

            holder.setTitle(mItem.displayName);
            holder.setQuantity("x"+String.valueOf(mItem.count));
            holder.mPrice.setText(String.valueOf(mItem.price)+" RUB");


            if (mItem.image != null) {
                Glide.with(mContext)
                    .load(mItem.image)
                    
                    ////.crossFade()
                    //.fitCenter()
                        .centerCrop()
                    .placeholder(R.drawable.placeholder_small)
                    .error(R.drawable.placeholder_small)
                    .into(holder.mImageView);
            }


            holder.card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.Interaction(mItem);
                }
            });


            holder.mChanger.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            holder.mPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.InteractionPlus(mItem);
                }
            });

            holder.mMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.InteractionMinus(mItem);
                }
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