package com.octarine.plove.reservation;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.StationModel;
import com.octarine.plove.cafes.CafesFragment;
import com.octarine.plove.cafes.OpenCafeActivity;
import com.octarine.plove.cafes.RecyclerCafeAdapter;
import com.octarine.plove.R;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.catalog.CategoryActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octarine.plove.catalog.CategoryActivity.EXTRA_FROM;
import static com.octarine.plove.catalog.CategoryActivity.FROM_RESERVATION;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ReservationSelectCafeActivity extends AppCompatActivity implements  SwipeRefreshLayout.OnRefreshListener {

    public static final String STATION_ADDRESS = "address";
    public static final String STATION_ID = "station_id";
    public RecyclerCafeAdapter adapter;
    private SwipeRefreshLayout swipeView;
    CafesFragment.InteractionListener mListener;
    public List<Object> mItems = new ArrayList<>();
    String launch_from = FROM_RESERVATION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_select_cafe);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        launch_from = getIntent().getExtras().getString(EXTRA_FROM);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeView = (SwipeRefreshLayout) findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.parseColor("#cf2022"), Color.parseColor("#8ACF1A"));
        swipeView.setDistanceToTriggerSync(20);
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);

        mItems = new ArrayList<>();
        mListener = object -> {
            if (launch_from.equalsIgnoreCase(FROM_RESERVATION)) {

                    Intent i = new Intent(ReservationSelectCafeActivity.this, ReservationProfileActivity.class);
                    i.putExtra(OpenCafeActivity.EXTRA_CAFE, Parcels.wrap(object));
                    startActivity(i);

            } else {
                PreferenceManager.getDefaultSharedPreferences(ReservationSelectCafeActivity.this)
                        .edit()
                        .putString(STATION_ID, ((StationModel) object).stationId)
                        .putString(STATION_ADDRESS, ((StationModel) object).address)
                        .apply();
                Intent i = new Intent(ReservationSelectCafeActivity.this, CategoryActivity.class);
                i.putExtra(CategoryActivity.EXTRA_FROM, CategoryActivity.FROM_TAKEAWAY);
                startActivity(i);
            }
        };
        adapter = new RecyclerCafeAdapter(this, mItems, mListener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        loadCafes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_select_cafe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_next) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public void loadCafes() {
        ApplicationController.getInstance().showLoading(ReservationSelectCafeActivity.this);
        ApplicationController.getApi().stations(AuthModel.getInstance(ReservationSelectCafeActivity.this).sessionId).enqueue(new Callback<List<StationModel>>() {
            @Override
            public void onResponse(Call<List<StationModel>> call, Response<List<StationModel>> response) {
                List<StationModel> list = response.body();
                Log.d("Load", response.raw().toString());
                if (response.isSuccessful()) {
                    mItems.clear();
                    mItems.addAll(list);
                    adapter.notifyDataSetChanged();
                }
                ApplicationController.getInstance().hideLoading();
                swipeView.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<StationModel>> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                swipeView.setRefreshing(false);
                ApplicationController.showError(ReservationSelectCafeActivity.this, getString(R.string.connection_error));
            }
        });
    }


    @Override
    public void onRefresh() {
        swipeView.setRefreshing(true);
        loadCafes();
    }
}
