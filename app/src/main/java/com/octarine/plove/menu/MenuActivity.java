package com.octarine.plove.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.preference.PreferenceManager;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
//import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.CatalogModel;
import com.octarine.plove.api.models.MenuModel;
import com.octarine.plove.catalog.CategoryActivity;
import com.octarine.plove.bag.OnUpdateBagListener;
import com.octarine.plove.R;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.bag.BagActivity;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octarine.plove.catalog.CategoryActivity.EXTRA_FROM;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MenuActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    Activity context;
    TextView mTitle;
    CoordinatorLayout coordinator;
    TextView mBagTitle;
    String launch_from  = CategoryActivity.FROM_DELIVERY;
    SwipeRefreshLayout swipeView;
    public RecyclerMenuItemsAdapter adapter;
    OnUpdateBagListener mOnUpdateBagListener;
    InteractionListener mListener;
    public ArrayList<Object> mItems = new ArrayList<Object>();
    CatalogModel category;

    public static final String EXTRA_CATEGORY = "EXTRA_CAT";

    public void setOnUpdateBagListener(OnUpdateBagListener mOnUpdateBagListener) {
        this.mOnUpdateBagListener = mOnUpdateBagListener;
    }

    @Override
    public void onRefresh() {
        swipeView.setRefreshing(true);
        pageRefreshData();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_new);
        this.context = this;

        ApplicationController.getInstance().hideLoading();

        launch_from = getIntent().getExtras().getString(EXTRA_FROM);
        category = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CATEGORY));

        coordinator =  findViewById(R.id.main_content);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mTitle =  toolbar.findViewById(R.id.textViewToolbarTitle);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        mBagTitle =  toolbar.findViewById(R.id.textViewBagTitle);
        mBagTitle.setOnClickListener(view -> {
            Intent i = new Intent(MenuActivity.this, BagActivity.class);
            startActivityForResult(i, 1);
        });
        ImageView imageViewShop =  toolbar.findViewById(R.id.imageViewShop);
        imageViewShop.setOnClickListener(view -> {
            Intent i = new Intent(MenuActivity.this, BagActivity.class);
            startActivityForResult(i, 1);
        });


        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setHomeButtonEnabled(true);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        swipeView = findViewById(R.id.swipe_view);
        swipeView.setOnRefreshListener(this);
        swipeView.setColorSchemeColors(Color.GRAY, Color.parseColor("#cf2022"), Color.parseColor("#8ACF1A"));
        swipeView.setDistanceToTriggerSync(20);
        swipeView.setSize(SwipeRefreshLayout.DEFAULT);

        mItems = new ArrayList<>();
        mListener = new InteractionListener() {
            @Override
            public void Interaction(Object object) {
                Intent i = new Intent(context, MenuItemActivity.class);
                i.putExtra(MenuItemActivity.EXTRA_MENUITEM, Parcels.wrap(object));
                i.putExtra(EXTRA_FROM, launch_from);
                context.startActivity(i);
            }

            @Override
            public void InteractionClick(Object object) {
                String from = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this).getString(EXTRA_FROM, "0");
                boolean basket_empty = ApplicationController.getInstance().getBasket(null, "").size() == 0;

                if (!basket_empty && !from.equalsIgnoreCase(launch_from) && !from.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                    builder.setTitle("Внимание!");
                    builder.setMessage("У Вас не пустая корзина и Вы добавляете в корзину позицию из другого типа меню. Очистить корзину и добавить?");
                    builder.setPositiveButton("Oчистить и добавить", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        ApplicationController.getInstance().clearBasket();
                        PreferenceManager.getDefaultSharedPreferences(MenuActivity.this)
                                .edit()
                                .putString(EXTRA_FROM, launch_from)
                                .apply();
                        ApplicationController.getInstance().addToBasket((MenuModel) object, mBagTitle);
                    });
                    builder.setNegativeButton("Отмена", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                    alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.plove_red));
                } else {
                    PreferenceManager.getDefaultSharedPreferences(MenuActivity.this)
                            .edit()
                            .putString(EXTRA_FROM, launch_from)
                            .apply();
                    ApplicationController.getInstance().addToBasket((MenuModel) object, mBagTitle);
                }
            }

            @Override
            public void InteractionPlus(Object object) {
                vibrate();
                String from = PreferenceManager.getDefaultSharedPreferences(MenuActivity.this).getString(EXTRA_FROM, "0");
                boolean basket_empty = ApplicationController.getInstance().getBasket(null, "").size() == 0;

                if (!basket_empty && !from.equalsIgnoreCase(launch_from) && !from.equalsIgnoreCase("0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                    builder.setTitle("Внимание!");
                    builder.setMessage("У Вас не пустая корзина и Вы добавляете в корзину позицию из другого типа меню. Очистить корзину и добавить?");
                    builder.setPositiveButton("Oчистить и добавить", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                        ApplicationController.getInstance().clearBasket();
                        PreferenceManager.getDefaultSharedPreferences(MenuActivity.this)
                                .edit()
                                .putString(EXTRA_FROM, launch_from)
                                .apply();
                        ApplicationController.getInstance().addToBasket((MenuModel) object, mBagTitle);
                    });
                    builder.setNegativeButton("Отмена", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                    alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.plove_red));
                } else {
                    PreferenceManager.getDefaultSharedPreferences(MenuActivity.this)
                            .edit()
                            .putString(EXTRA_FROM, launch_from)
                            .apply();
                    ApplicationController.getInstance().addToBasket((MenuModel) object, mBagTitle);
                }
            }

            @Override
            public void InteractionMinus(Object object) {
                vibrate();
                ApplicationController.getInstance().removeFromBasket((MenuModel) object, mBagTitle);
            }
        };
        loadMenu();
        updateBasket();


        adapter = new RecyclerMenuItemsAdapter(this, mItems, mListener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        toolbar.setTitle(category.displayName);
        setTitle(category.displayName);
        mTitle.setText(category.displayName);
    }

    private void updateBasket() {
        ApplicationController.getInstance().getBasket(mBagTitle, " руб ");
    }

    public void loadMenu() {
        ApplicationController.getInstance().showLoading(MenuActivity.this);
        ApplicationController.getApi().menu(AuthModel.getInstance(MenuActivity.this).sessionId,
                launch_from, category.crmId).enqueue(new Callback<List<MenuModel>>() {
            @Override
            public void onResponse(Call<List<MenuModel>> call, Response<List<MenuModel>> response) {
                Log.d("ATG", response.raw().toString());
                List<MenuModel> list = response.body();
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
            public void onFailure(Call<List<MenuModel>> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                swipeView.setRefreshing(false);
                ApplicationController.showError(MenuActivity.this, getString(R.string.connection_error));
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public void onResume(){
        super.onResume();
        updateBasket();
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_menu, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void pageRefreshData() {
        int offset = 0;
        int limit = 100;
        loadMenu();
    }

    interface InteractionListener {
        void Interaction(Object object);
        void InteractionClick(Object object);
        void InteractionPlus(Object object);
        void InteractionMinus(Object object);
    }

    public void vibrate() {
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
        }else{
            v.vibrate(100);
        }
    }
}
