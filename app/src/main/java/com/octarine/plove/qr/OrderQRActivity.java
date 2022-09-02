package com.octarine.plove.qr;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import com.octarine.plove.api.models.OrderStepItem;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.R;
import com.octarine.plove.pay.WebViewActivity;
import com.octarine.plove.reservation.RecycleStepAdapter;
import com.octarine.plove.reservation.ReservationProfileActivity;
import java.util.ArrayList;
import java.util.List;

import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_KEY_VALUE;
import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_SEEK;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderQRActivity extends AppCompatActivity {

    public RecycleStepAdapter adapter;
    ReservationProfileActivity.InteractionListener mListener;

    TextView tvPrice;
    TextView tvOrder;
    float basketPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_qr);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<OrderStepItem> objects = new ArrayList<>();

        int progressMax = 0;
        basketPrice = getIntent().getFloatExtra("price", 0);


        ProfileModel user = ProfileModel.getInstance(OrderQRActivity.this);
        int balance = Integer.parseInt(user.balance);
        progressMax = Math.round(basketPrice / 2);
        if (progressMax > balance) progressMax = balance;

        objects.add(new OrderStepItem(2, "Заказ на сумму:", basketPrice + " руб.", ITEM_TYPE_KEY_VALUE));
        objects.add(new OrderStepItem(6, "Тип оплаты", getPayType(), ITEM_TYPE_KEY_VALUE));
        objects.add(new OrderStepItem(7, "Скидка", "- 0 RUB", ITEM_TYPE_SEEK, progressMax));

        mListener = new ReservationProfileActivity.InteractionListener() {
            @Override
            public void InteractionTypeEdit(OrderStepItem object, String value) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void InteractionTypeChanger(OrderStepItem object, String value) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void InteractionTypeKeyValue(OrderStepItem object) {

            }

            @Override
            public void InteractionTypeSelector(OrderStepItem object) {

            }

            @Override
            public void InteractionTypeSlider(OrderStepItem object, String value) {

            }
        };

        adapter = new RecycleStepAdapter(this, objects, mListener);

        tvPrice = findViewById(R.id.textViewBugPrice);
        tvOrder = findViewById(R.id.textViewOrder);
        recyclerView.setAdapter(adapter);
        tvOrder.setOnClickListener(view -> {
                Intent intent = new Intent(OrderQRActivity.this, WebViewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        });

        updateBag();
    }

    public void updateBag(){
        tvPrice.setText(String.format("%s.", basketPrice));
    }

    @Override
    public void onResume(){
        super.onResume();
    }


    public String getPayType() {
        return "Банковской картой";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_next) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
