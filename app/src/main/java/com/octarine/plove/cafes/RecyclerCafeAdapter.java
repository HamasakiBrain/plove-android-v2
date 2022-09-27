package com.octarine.plove.cafes;

import android.content.Context;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.octarine.plove.api.models.StationModel;
import com.octarine.plove.common.OnLoadMoreListener;
import com.octarine.plove.R;
import com.octarine.plove.menu.RecyclerLoadingViewHolder;
import java.util.List;

public class RecyclerCafeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 3;
    private int lastVisibleItem, totalItemCount;

    private List<Object> mItemList;
    private CafesFragment.InteractionListener mListener;
    private Context mContext;

    public RecyclerCafeAdapter(Context context, List<Object> itemList, CafesFragment.InteractionListener listener) {
        mItemList = itemList;
        mListener = listener;
        mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView mRecyclerView = recyclerView;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_cafe_item, parent, false);
            return RecyclerCafeItemViewHolder.newInstance(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_load, parent, false);
            return new RecyclerLoadingViewHolder(view);
        }
        return null;
    }

    void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    private Object getItem(int i) {
        return mItemList.get(i);
    }

    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof RecyclerCafeItemViewHolder) {

            final StationModel cafe = (StationModel) getItem(position);

            RecyclerCafeItemViewHolder holder = (RecyclerCafeItemViewHolder) viewHolder;
            holder.setTextViewTitleBlack(cafe.displayName);
            holder.setTextViewTitlePreview(cafe.work_time);
            if (cafe.image.android != null) {
                Glide.with(mContext)
                        .load(cafe.image.android)
                        //
                        //.crossFade()
                        //.fitCenter()
                        .placeholder(R.drawable.big)
                        .into(holder.mImageViewPreview);
            }


            holder.card.setOnClickListener(view -> mListener.Interaction(cafe));

        } else {
            if (viewHolder instanceof RecyclerLoadingViewHolder) {
                RecyclerLoadingViewHolder loadingViewHolder = (RecyclerLoadingViewHolder) viewHolder;
                loadingViewHolder.progressBar.setIndeterminate(true);
            }
        }


    }

}