package com.octarine.plove.profile;

/**
 * Created by rustam on 05.04.2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;

import java.util.List;

public class ProfileServicesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Object> mItemList;

    public  static final int TYPE_ITEM = 1;

    private ProfileFragment.OnListFragmentInteractionListener mListener;
    private Context mContext;

    public ProfileServicesAdapter(Context context, List<Object> itemList, ProfileFragment.OnListFragmentInteractionListener listener
    ) {
        mItemList = itemList;
        mListener = listener;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_service_item, parent, false);
            return ServiceItemViewHolder.newInstance(view);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {


        if (viewHolder instanceof ServiceItemViewHolder) {
            Service s = getItem(position);
            ServiceItemViewHolder holder = (ServiceItemViewHolder) viewHolder;
            holder.mTitle.setText(s.title);
            holder.mImage.setImageResource(s.image);
            holder.mCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onServiceClick(s);
                }
            });
        }

    }
    public Service getItem(int i) {
        return (Service) mItemList.get(i);
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