package com.octarine.plove.cafes;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.StationModel;

import com.octarine.plove.R;
import com.octarine.plove.app.ApplicationController;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Callback;


public class CafesFragment extends Fragment implements  SwipeRefreshLayout.OnRefreshListener{

    public RecyclerCafeAdapter adapter;
    private SwipeRefreshLayout swipeView;
    InteractionListener mListener;
    public List<Object> mItems = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_cafes, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeView = view.findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.parseColor("#cf2022"), Color.parseColor("#8ACF1A"));
        swipeView.setDistanceToTriggerSync(20);
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);

        mItems = new ArrayList<>();
        mListener = object -> {
            Intent i = new Intent(getActivity(), OpenCafeActivity.class);
            i.putExtra(OpenCafeActivity.EXTRA_CAFE, Parcels.wrap(object));
            startActivity(i);
        };

        adapter = new RecyclerCafeAdapter(getActivity(), mItems, mListener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //adapter.setOnLoadMoreListener(() -> pageNextData());
        swipeView.setRefreshing(true);
        loadCafes();
        return view;
    }

    public void loadCafes() {

        ApplicationController.getApi().stations(AuthModel.getInstance(getActivity()).sessionId).enqueue(new Callback<List<StationModel>>() {
            @Override
            public void onResponse(Call<List<StationModel>> call, Response<List<StationModel>> response) {
                List<StationModel> list = response.body();
                Log.d("Load", response.raw().toString());
                if (response.isSuccessful()) {
                    mItems.clear();
                    mItems.addAll(list);
                    adapter.notifyDataSetChanged();
                }

                swipeView.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<StationModel>> call, Throwable t) {
                swipeView.setRefreshing(false);
                ApplicationController.showError(getActivity(), getString(R.string.connection_error));
            }
        });
    }

    private void pageNextData() {
        //int offset = RealmController.with(getActivity()).lastCafeId();
        //int limit = 10;
        //loadNewData(offset, limit, false);
    }

    private void pageRefreshData() {
        int offset = 0;
        int limit = 10;
        loadCafes();
    }

    @Override
    public void onRefresh() {
        swipeView.setRefreshing(true);
        pageRefreshData();
    }

    public interface InteractionListener {
        void Interaction(Object object);
    }

}
