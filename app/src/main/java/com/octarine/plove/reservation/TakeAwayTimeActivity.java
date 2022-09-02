package com.octarine.plove.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.octarine.plove.R;
import com.octarine.plove.api.models.Order;
import com.octarine.plove.api.models.OrderStepItem;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.common.RangeTimePickerDialog;
import com.octarine.plove.order.OrderStep1Activity;
import com.octarine.plove.order.OrderStepResumeActivity;

import org.parceler.Parcels;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.octarine.plove.order.OrderStep1Activity.EXTRA_ORDER;
import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_CHANGER;
import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_EDIT;
import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_HEADER;
import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_KEY_VALUE;
import static com.octarine.plove.reservation.ReservationSelectCafeActivity.STATION_ADDRESS;
import static com.octarine.plove.reservation.ReservationSelectCafeActivity.STATION_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TakeAwayTimeActivity extends AppCompatActivity {

    public static final int RESERVE_USERNAME = 1;
    public static final int RESERVE_PHONE = 3;
    public static final int RESERVE_PERSONS_COUNT = 4;
    public static final int RESERVE_DATETIME = 6;
    public static final int RESERVE_DESCRIPTION = 9;
    public static final int RESERVE_HEADER = 10;

    public RecycleStepAdapter adapter;
    public RelativeLayout rootView;
    public List<OrderStepItem> objects;

    ReservationProfileActivity.InteractionListener mListener;
    private Order order;

    public String addNull(int value) {
        if (value < 10)
            return "0" + value; else return String.valueOf(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        rootView = (RelativeLayout) findViewById(R.id.content_reservation_profile);
        order = (Order) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_ORDER));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ProfileModel user = ProfileModel.getInstance(TakeAwayTimeActivity.this);

        order.datetime = "";
        order.username = user.firstname + " " + user.lastname;
        order.phone = user.phone;

        order.station_id = PreferenceManager.getDefaultSharedPreferences(TakeAwayTimeActivity.this).getString(STATION_ID, "0");
        order.address = PreferenceManager.getDefaultSharedPreferences(TakeAwayTimeActivity.this).getString(STATION_ADDRESS, "0");
        order.persons = "1";

        objects = new ArrayList<>();


        objects.add(new OrderStepItem(RESERVE_HEADER, "Выберите время", false, ITEM_TYPE_HEADER)); //0
        objects.add(new OrderStepItem(RESERVE_DATETIME, "Не выбрано", "Выбрать", ITEM_TYPE_KEY_VALUE)); //1
        objects.add(new OrderStepItem(RESERVE_HEADER, "Выберите количество персон", false, ITEM_TYPE_HEADER)); //2
        objects.add(new OrderStepItem(RESERVE_PERSONS_COUNT, "Количество персон", "1", false,  ITEM_TYPE_CHANGER)); //3
        objects.add(new OrderStepItem(RESERVE_HEADER, "Как к Вам обращаться?", false, ITEM_TYPE_HEADER)); //4
        objects.add(new OrderStepItem(RESERVE_USERNAME, "Имя", user.firstname + " " + user.lastname, false,  ITEM_TYPE_EDIT)); //5
        objects.add(new OrderStepItem(RESERVE_PHONE, "Телефон", "+" + user.phone, false,  ITEM_TYPE_EDIT)); //6
        objects.add(new OrderStepItem(RESERVE_DESCRIPTION, "Дополнительная информация", "", false, ITEM_TYPE_EDIT)); //7

        mListener = new ReservationProfileActivity.InteractionListener() {
            @Override
            public void InteractionTypeEdit(OrderStepItem object, String value) {
                if (object.id == RESERVE_USERNAME) {
                    order.username = value;
                    objects.set(5, new OrderStepItem(RESERVE_USERNAME, "Имя", value, false,  ITEM_TYPE_EDIT));
                }
                if (object.id == RESERVE_PHONE) {
                    order.phone = value;
                    objects.set(6, new OrderStepItem(RESERVE_PHONE, "Телефон", value, false,  ITEM_TYPE_EDIT));
                }
                if (object.id == RESERVE_DESCRIPTION) {
                    order.info = order.info + value;
                    objects.set(7, new OrderStepItem(RESERVE_DESCRIPTION, "Дополнительная информация", value, false, ITEM_TYPE_EDIT));
                }
                try {
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }

            @Override
            public void InteractionTypeChanger(OrderStepItem object, String value) {
                if (object.id == RESERVE_PERSONS_COUNT) {
                    order.persons = value;
                    objects.set(3, new OrderStepItem(RESERVE_PERSONS_COUNT, "Количество персон", value, false,  ITEM_TYPE_CHANGER));
                }
                try {
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
            }

            @Override
            public void InteractionTypeKeyValue(OrderStepItem object) {
                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE, 45);
                RangeTimePickerDialog picker = new RangeTimePickerDialog(TakeAwayTimeActivity.this, R.style.AppCompatAlertDialogStyle,
                        (view, hourOfDay, minute) -> {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                            String today = sdf.format(new Date());
                            //String today = addNull(now.getTime().getDay()) + "." + addNull(now.getTime().getMonth())+ "." + addNull(now.getTime().getYear());
                            String selectedTime = addNull(hourOfDay) + ":" + addNull(minute);
                            order.datetime = today + " " + addNull(hourOfDay) + ":" + addNull(minute)+":00";
                            Log.d("Reservation", order.datetime);
                            objects.set(1, new OrderStepItem(RESERVE_DATETIME, selectedTime, "Изменить", ITEM_TYPE_KEY_VALUE));
                            try {
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {

                            }
                        }, now.getTime().getHours(), now.getTime().getMinutes(), true);
                picker.setMax(23, 55);
                if (now.getTime().getHours() < 11)
                    picker.setMin(11, 00);
                else
                    picker.setMin(now.getTime().getHours(), now.getTime().getMinutes());
                picker.show();
            }

            @Override
            public void InteractionTypeSelector(OrderStepItem object) {

            }

            @Override
            public void InteractionTypeSlider(OrderStepItem object, String value) {

            }
        };

        adapter = new RecycleStepAdapter(this, objects, mListener);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_cafe, menu);
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
            if (!order.datetime.isEmpty()) {
                if (check(order, rootView)) {
                    Intent i = new Intent(TakeAwayTimeActivity.this, OrderStepResumeActivity.class);
                    i.putExtra(OrderStep1Activity.EXTRA_ORDER, Parcels.wrap(order));
                    startActivity(i);
                }
            } else {
                ApplicationController.getInstance().showMessage(rootView, "Выберите желаемое время брони.");
            }
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    public boolean check(Order order, View view) {

        if (order.username.isEmpty()) {
            ApplicationController.getInstance().showMessage(rootView, "Введите Ваше имя");
            return false;
        }
        if (order.phone.isEmpty()) {
            ApplicationController.getInstance().showMessage(rootView, "Введите Ваш телефон");
            return false;
        }
        if (order.datetime.isEmpty() || order.datetime.equalsIgnoreCase("Выбрать")) {
            ApplicationController.getInstance().showMessage(rootView, "Выберите время бронирования");
            return false;
        }
        return true;
    }
}
