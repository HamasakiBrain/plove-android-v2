package com.octarine.plove.order;

import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.octarine.plove.api.models.Order;
import com.octarine.plove.api.models.OrderStepItem;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.R;
import com.octarine.plove.reservation.RecycleStepAdapter;
import com.octarine.plove.reservation.ReservationProfileActivity;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.octarine.plove.order.OrderStep1Activity.EXTRA_ORDER;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class OrderStepResumeActivity extends AppCompatActivity {

    private static final int DELIVERY_PERSONS = 30;
    public RecycleStepAdapter adapter;

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    public static final int ITEM_TYPE_EDIT = 2;
    public static final int ITEM_TYPE_CHANGER = 3;
    public static final int ITEM_TYPE_KEY_VALUE = 4;
    public static final int ITEM_TYPE_SEEK = 5;

    TextView tvPrice;
    TextView tvOrder;
    Order order;
    ReservationProfileActivity.InteractionListener mListener;
    int progressMax = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_step_resume);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<OrderStepItem> objects = new ArrayList<>();
        order = (Order) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_ORDER));

        ProfileModel user = ProfileModel.getInstance(OrderStepResumeActivity.this);

        int balance = Integer.valueOf(user.balance);
        int basketPrice = 1000;
        progressMax = Math.round(basketPrice / 2);
        if (progressMax > balance) progressMax = balance;


        if (order.delivery_type.equalsIgnoreCase("1"))
            objects.add(new OrderStepItem(1, "Адрес", order.city + ", " + order.street + ", " + order.build + ", " + order.room, ITEM_TYPE_KEY_VALUE));
        else
            objects.add(new OrderStepItem(1, "Адрес", order.address, false, ITEM_TYPE_KEY_VALUE));
        objects.add(new OrderStepItem(2, "Контактное лицо", order.username, false, ITEM_TYPE_KEY_VALUE));
        objects.add(new OrderStepItem(3, "Телефон", order.phone, false, ITEM_TYPE_KEY_VALUE));
        objects.add(new OrderStepItem(DELIVERY_PERSONS, "Количество персон", "1", false,  ITEM_TYPE_CHANGER));
        objects.add(new OrderStepItem(5, "Тип доставки", getDeliveryType(), false, ITEM_TYPE_KEY_VALUE));
        objects.add(new OrderStepItem(6, "Тип оплаты", getPayType(), false, ITEM_TYPE_KEY_VALUE));
        objects.add(new OrderStepItem(7, "Скидка", "- 0 RUB", ITEM_TYPE_SEEK, progressMax));

        mListener = new ReservationProfileActivity.InteractionListener() {
            @Override
            public void InteractionTypeEdit(OrderStepItem object, String value) {

            }

            @Override
            public void InteractionTypeChanger(OrderStepItem object, String value) {
                if (object.id == DELIVERY_PERSONS) {
                    order.persons = value;
                    objects.set(3, new OrderStepItem(DELIVERY_PERSONS, "Количество персон", value, false,  ITEM_TYPE_CHANGER));
                }
                try {
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }

            @Override
            public void InteractionTypeKeyValue(OrderStepItem object) {

            }

            @Override
            public void InteractionTypeSelector(OrderStepItem object) {

            }

            @Override
            public void InteractionTypeSlider(OrderStepItem object, String value) {
                if (object.id == 7) {
                    order.discount = Integer.valueOf(value);
                    order.total = order.amount - order.discount;
                    objects.set(6, new OrderStepItem(7, "Скидка", "- " + value + " RUB", ITEM_TYPE_SEEK, progressMax));
                    tvPrice.setText(String.valueOf(order.total));
                }
                try {
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }
        };

        adapter = new RecycleStepAdapter(this, objects, mListener);
        tvPrice = (TextView) findViewById(R.id.textViewBugPrice);
        tvOrder = (TextView) findViewById(R.id.textViewOrder);
        recyclerView.setAdapter(adapter);
        tvOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if (ApplicationController.SELECTED_PAY_TYPE == ApplicationController.PAY_TYPE_CARD) {
                //    Intent intent = new Intent(OrderStepResumeActivity.this, OrderStep3Activity.class);
                //    startActivity(intent);
                //} else {
                //Intent intent = new Intent(OrderStepResumeActivity.this, WebViewActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //startActivity(intent);
                //}
            }
        });
        updateBag();
        tvPrice.setText(String.valueOf(order.total));

    }

    public void updateBag(){
        //ApplicationController.getInstance().updateBag(true);
        //tvPrice.setText(ApplicationController.getInstance().updateBagWithDiscount() + ".");
    }

    @Override
    public void onResume(){
        super.onResume();
        // put your code here...
        updateBag();
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
            //if (adapter.check()) {
            //   Intent i = new Intent(OrderStepResumeActivity.this, OrderStep3Activity.class);
            //    startActivity(i);
            //}
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    String getDeliveryType() {
        if (order.delivery_type.equalsIgnoreCase("1")) return  "Доставка";
        if (order.delivery_type.equalsIgnoreCase("2")) return  "Заберу сам";
        return "Предзаказ";
    }

    String getPayType() {
        if (order.pay_type.equalsIgnoreCase("1")) return  "Банковской картой";
        if (order.pay_type.equalsIgnoreCase("2")) return  "Наличными";
        return "Картой курьеру";
    }

}
