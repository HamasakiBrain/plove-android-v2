package com.octarine.plove.catalog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.util.Log;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.CatalogModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.menu.MenuActivity;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryActivity extends AppCompatActivity {


    Context context;
    RelativeLayout coordinator;
    RecyclerCategoryAdapter adapter;
    public static final String EXTRA_FROM = "EXTRA_FROM";
    public static final String FROM_DELIVERY = "catalog_delivery";
    public static final String FROM_TAKEAWAY = "catalog_with_self";
    public static final String FROM_RESERVATION = "reservation";
    public static final String FROM_INCAFE = "catalog_in_cafe";

    InteractionListener mListener;
    public List<Object> mItems = new ArrayList<>();
    String launch_from = CategoryActivity.FROM_DELIVERY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        context = this;
        mItems = new ArrayList<>();

        launch_from = getIntent().getExtras().getString(EXTRA_FROM);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        coordinator =  findViewById(R.id.coordinator);

        mListener = object -> {
            Intent i = new Intent(context, MenuActivity.class);
            i.putExtra(MenuActivity.EXTRA_CATEGORY, Parcels.wrap(object));
            i.putExtra(EXTRA_FROM, launch_from);
            startActivity(i);
        };

        adapter = new RecyclerCategoryAdapter(this, mItems, mListener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadCatalog();
    }

    public void loadCatalog() {
        ApplicationController.getInstance().showLoading(CategoryActivity.this);
        ApplicationController.getApi().catalog(AuthModel.getInstance(CategoryActivity.this).sessionId,
                launch_from).enqueue(new Callback<List<CatalogModel>>() {
            @Override
            public void onResponse(Call<List<CatalogModel>> call, Response<List<CatalogModel>> response) {
                List<CatalogModel> list = response.body();
                if (response.isSuccessful()) {
                    mItems.clear();
                    mItems.addAll(list);
                    adapter.notifyDataSetChanged();
                }
                ApplicationController.getInstance().hideLoading();
            }

            @Override
            public void onFailure(Call<List<CatalogModel>> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(CategoryActivity.this, getString(R.string.connection_error));
            }
        });
    }

    interface InteractionListener {
        void Interaction(Object object);
    }

}
