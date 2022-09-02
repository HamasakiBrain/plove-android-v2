package com.octarine.plove.history;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.HistoryResponseModel;
import com.octarine.plove.R;
import com.octarine.plove.app.ApplicationController;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerHistoryAdapter adapter;
    private SwipeRefreshLayout swipeView;
    TextView emptyHistory;
    public List<HistoryResponseModel.History> mItems = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_history, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        swipeView = view.findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.parseColor("#cf2022"), Color.parseColor("#8ACF1A"));
        swipeView.setDistanceToTriggerSync(20);
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);


        adapter = new RecyclerHistoryAdapter(mItems);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        emptyHistory = view.findViewById(R.id.textViewEmptyHistory);
        if (mItems.size() > 0) {
            emptyHistory.setVisibility(View.GONE);
        } else {
            emptyHistory.setVisibility(View.VISIBLE);
        }
        pageRefreshData();
        swipeView.setRefreshing(true);
        return view;
    }

    private void pageRefreshData() {
        int offset = 0;
        int limit = 1000;
        loadData(offset, limit, true);
    }

    private void loadData(int offset, int limit, final boolean clear) {
        ApplicationController.getApi().history(AuthModel.getInstance(getActivity()).sessionId, limit, offset, "").enqueue(new Callback<HistoryResponseModel>() {
            @Override
            public void onResponse(Call<HistoryResponseModel> call, Response<HistoryResponseModel> response) {
                Log.d("History", response.raw().toString());
                if (response.isSuccessful()) {
                    //if (clear)
                    HistoryResponseModel list = response.body();
                    mItems.clear();
                    mItems.addAll(list.history);
                    adapter.notifyDataSetChanged();
                    if (mItems.size() > 0) {
                        emptyHistory.setVisibility(View.GONE);
                    } else {
                        emptyHistory.setVisibility(View.VISIBLE);
                    }
                }
                swipeView.setRefreshing(false);
            }
            @Override
            public void onFailure(Call<HistoryResponseModel> call, Throwable t) {
                swipeView.setRefreshing(false);
                ApplicationController.showError(getActivity(), getString(R.string.connection_error));
            }
        });
    }

    @Override
    public void onRefresh() {
        swipeView.setRefreshing(true);
        pageRefreshData();
    }

}
