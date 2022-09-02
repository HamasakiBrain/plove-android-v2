package com.octarine.plove.bag;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.octarine.plove.api.models.BagItem;
import com.octarine.plove.api.models.Order;
import com.octarine.plove.app.ApplicationController;
//import com.octarine.plove.menu.MenuItemActivity;
import com.octarine.plove.order.OrderAddressActivity;
import com.octarine.plove.order.OrderStep1Activity;
import com.octarine.plove.R;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;

import static com.octarine.plove.catalog.CategoryActivity.EXTRA_FROM;
import static com.octarine.plove.catalog.CategoryActivity.FROM_DELIVERY;
import static com.octarine.plove.catalog.CategoryActivity.FROM_INCAFE;
import static com.octarine.plove.catalog.CategoryActivity.FROM_TAKEAWAY;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BagActivity extends AppCompatActivity {

    RecyclerBagAdapter adapter;
    TextView tvPrice;
    TextView tvOrder;
    TextView tvEmptyBag;
    InteractionListener mListener;
    public List<Object> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bag);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        tvPrice = findViewById(R.id.textViewBugPrice);

        mItems = new ArrayList<>();
        mListener = new InteractionListener() {
            @Override
            public void Interaction(Object object) {
                //Intent i = new Intent(BagActivity.this, MenuItemActivity.class);
                //i.putExtra(MenuItemActivity.EXTRA_MENUITEM, Parcels.wrap(object));
                //startActivity(i);
            }

            @Override
            public void InteractionClick(Object object) {

            }

            @Override
            public void InteractionPlus(Object object) {
                ApplicationController.getInstance().addToBasketBag((BagItem) object, tvPrice);
                updateBasket();
            }

            @Override
            public void InteractionMinus(Object object) {
                ApplicationController.getInstance().removeFromBasketBag((BagItem) object, tvPrice);
                updateBasket();
            }
        };

        setTitle("Корзина");

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecyclerBagAdapter(this, mItems, mListener);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        tvOrder =  findViewById(R.id.textViewOrder);
        tvEmptyBag =  findViewById(R.id.textViewEmptyBag);
        updateBasket();

        tvOrder.setOnClickListener(view -> {

            Order order = new Order();
            order.city = "-";
            order.street = "-";
            order.build = "-";
            order.corpus = "-";
            order.info = "-";
            order.room = "-";

            String mode = PreferenceManager.getDefaultSharedPreferences(BagActivity.this).getString(EXTRA_FROM, FROM_DELIVERY);
            if (mode.equalsIgnoreCase(FROM_DELIVERY)) {
                if ((mItems.size() > 0) && (Integer.parseInt(tvPrice.getText().toString()) > 2)){
                    Intent i = new Intent(BagActivity.this, OrderAddressActivity.class);
                    startActivity(i);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(BagActivity.this);
                    builder.setTitle("Внимание");
                    builder.setMessage("Минимальная сумма заказа 1000 рублей");
                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                }
            }
            if (mode.equalsIgnoreCase(FROM_TAKEAWAY)) {
                if ((mItems.size() > 0) && (Integer.parseInt(tvPrice.getText().toString()) > 0)) {
                    Intent i = new Intent(BagActivity.this, OrderStep1Activity.class);
                    i.putExtra(OrderStep1Activity.EXTRA_ORDER, Parcels.wrap(order));
                    startActivity(i);
                }
            }
            if (mode.equalsIgnoreCase(FROM_INCAFE)) {
                if ((mItems.size() > 0) && (Integer.parseInt(tvPrice.getText().toString()) > 0)) {
                    Intent i = new Intent(BagActivity.this, OrderStep1Activity.class);
                    i.putExtra(OrderStep1Activity.EXTRA_ORDER, Parcels.wrap(order));
                    startActivity(i);
                }
            }
        });

        updateBasket();

    }

    @Override
    public void onResume(){
        super.onResume();
        updateBasket();
    }

    public void updateBasket() {
        ArrayList basket = ApplicationController.getInstance().getBasket(tvPrice, "");
        mItems.clear();
        if (basket!=null) {
            for(Object o_model: basket) {
                BagItem model = (BagItem) o_model;
                mItems.add(model);
            }
        }
        if (mItems.size() > 0) tvEmptyBag.setVisibility(View.GONE); else
            tvEmptyBag.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed()
    {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("result", "result");
        setResult(RESULT_OK, returnIntent);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bag, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_trash) {


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Очистка корзины");
            builder.setMessage("Удалить все позиции в корзине?");

            builder.setPositiveButton("Да", (dialogInterface, i) -> {
                ApplicationController.getInstance().clearBasket();
                updateBasket();
            });
            builder.setNegativeButton("Нет", (dialogInterface, i) -> {

            });

            AlertDialog alert = builder.create();
            alert.show();
            alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
            alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.plove_red));

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    interface InteractionListener {
        void Interaction(Object object);
        void InteractionClick(Object object);
        void InteractionPlus(Object object);
        void InteractionMinus(Object object);
    }


}
