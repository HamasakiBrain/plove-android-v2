package com.octarine.plove.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.octarine.plove.R;
import com.octarine.plove.api.models.NewsResponseModel;

import java.util.ArrayList;
import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mItemList;

    public  static final int TYPE_HEADER = 0;
    public  static final int TYPE_ITEM = 1;

    private ProfileFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public ProfileAdapter(Context context, List<Object> itemList, ProfileFragment.OnListFragmentInteractionListener listener
    ) {
        mItemList = itemList;
        mListener = listener;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_profile_item, parent, false);
            return ProfileItemViewHolder.newInstance(view);
        } else if (viewType == TYPE_HEADER) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_profile_header, parent, false);
            return ProfileHeaderViewHolder.newInstance(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {


        if (viewHolder instanceof ProfileItemViewHolder) {
            NewsResponseModel model = (NewsResponseModel) getItem(position);
            ProfileItemViewHolder holder = (ProfileItemViewHolder) viewHolder;
            holder.container.setOnClickListener(view -> mListener.onDealClick(model));
            holder.mTitle.setText(model.displayName);
            holder.mDesc.setText(model.preview);
            if (model.image.android != null) {
                Glide.with(mContext)
                        .load(model.image.android)
                        .crossFade()
                        .placeholder(R.drawable.big)
                        .into(holder.mImage);
            }

        } else if (viewHolder instanceof ProfileHeaderViewHolder) {
            String balance = (String) getItem(position);
            ProfileHeaderViewHolder holder = (ProfileHeaderViewHolder) viewHolder;
            List<Object> items = new ArrayList<>();
            items.add(new Service("Меню\n" +
                    "доставки", R.drawable.delivery, 1));
            items.add(new Service("Забрать\n" +
                    "с собой", R.drawable.take_away, 2));
            items.add(new Service("Бронь стола и \n" +
                    "предзаказ", R.drawable.reserved, 3));
            items.add(new Service("Начислить\n" +
                    "баллы", R.drawable.scan, 4));
            items.add(new Service("Оставить\n" +
                    "пожелания", R.drawable.email, 5));
            ProfileServicesAdapter adapter = new ProfileServicesAdapter(mContext, items, mListener);
            holder.recyclerView.setAdapter(adapter);
            holder.mScanner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onServiceClick(new Service("", 0, 4));
                }
            });
            holder.mBalance.setText(balance);
        }

    }
    private Object getItem(int i) {
        return mItemList.get(i);
    }
    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }
    @Override
    public int getItemViewType(int position) {
        if (position == 0)  return TYPE_HEADER;
        return TYPE_ITEM;
    }

}