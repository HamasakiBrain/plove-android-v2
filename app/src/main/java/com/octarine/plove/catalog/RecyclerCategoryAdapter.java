package com.octarine.plove.catalog;

import android.content.Context;
import android.content.res.Resources;
//import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;
import com.octarine.plove.api.models.CatalogModel;

import java.util.List;

class RecyclerCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private List<Object> mItemList;
    private CategoryActivity.InteractionListener mListener;
    private Context mContext;

    RecyclerCategoryAdapter(Context context, List<Object> itemList, CategoryActivity.InteractionListener listener) {
        mItemList = itemList;
        mListener = listener;
        mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_category, parent, false);
            return RecyclerCategoryViewHolder.newInstance(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        if (viewHolder instanceof RecyclerCategoryViewHolder) {

            CatalogModel mItem = (CatalogModel) getItem(position);
            final RecyclerCategoryViewHolder holder = (RecyclerCategoryViewHolder) viewHolder;
            String title = mItem.displayName;
            holder.setTitle(title);

            if (mItem.image != null) {
                Resources resources = mContext.getResources();
                final int resourceId = resources.getIdentifier(mItem.image, "drawable",
                        mContext.getPackageName());
                if (resourceId != 0)
                    holder.mImageView.setImageDrawable(resources.getDrawable(resourceId));
            }


            holder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.Interaction(mItem);
                }
            });
        }
    }

    private Object getItem(int i) {
        return mItemList.get(i);
    }

    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

}