package com.octarine.plove.order;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.octarine.plove.R;
import com.octarine.plove.api.models.BagItem;
import com.octarine.plove.api.models.Order;
import com.octarine.plove.api.models.OrderStepItem;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.common.ObjectSerializer;
import com.octarine.plove.common.RangeTimePickerDialog;
import com.octarine.plove.pay.WebViewActivity;
import com.octarine.plove.reservation.RecycleStepAdapter;
import com.octarine.plove.reservation.ReservationProfileActivity;


import org.parceler.Parcels;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.octarine.plove.catalog.CategoryActivity.EXTRA_FROM;
import static com.octarine.plove.catalog.CategoryActivity.FROM_DELIVERY;
import static com.octarine.plove.catalog.CategoryActivity.FROM_INCAFE;
import static com.octarine.plove.catalog.CategoryActivity.FROM_TAKEAWAY;
import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_CHANGER;
import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_EDIT;
import static com.octarine.plove.reservation.RecycleStepAdapter.ITEM_TYPE_KEY_VALUE;
import static com.octarine.plove.reservation.ReservationSelectCafeActivity.STATION_ADDRESS;
import static com.octarine.plove.reservation.ReservationSelectCafeActivity.STATION_ID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class OrderStep1Activity extends AppCompatActivity {

    public RecycleStepAdapter adapter;

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;

    public static final int DELIVERY_TYPE_DELIVERY = 1;
    public static final int DELIVERY_TYPE_SELF = 2;
    public static final int DELIVERY_TYPE_PLACE = 3;

    public static final int PAY_TYPE_CARD = 4;
    public static final int PAY_TYPE_CASH = 5;
    public static final int PAY_TYPE_CARD_COUR = 6;

    public static final int ACCEPTION_OPERATOR = 7;
    public static final int ACCEPTION_SMS = 8;

    public static final int RESERVE_USERNAME = 9;
    public static final int RESERVE_PHONE = 10;
    public static final int RESERVE_PERSONS_COUNT = 11;
    public static final int RESERVE_DATETIME = 12;
    public static final int RESERVE_DESCRIPTION = 13;
    public static final int RESERVE_HEADER = 14;
    public static final int DISCOUNT_TYPE_NONE = 18;
    public static final int DISCOUNT_TYPE_USE = 19;

    public static final String EXTRA_ORDER  = "order";
    int balance = 0;
    int can_use = 0;

    public Order order;// = new Order();
    ReservationProfileActivity.InteractionListener mListener;
    CoordinatorLayout rootView;

    public String addNull(int value) {
        if (value < 10)
            return "0" + value; else return String.valueOf(value);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_step1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        rootView = (CoordinatorLayout) findViewById(R.id.rootView);

        order = (Order) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_ORDER));

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        ArrayList basket  = new ArrayList();
        SharedPreferences prefs = getSharedPreferences("Application", Context.MODE_PRIVATE);
        try {
            basket = (ArrayList) ObjectSerializer.deserialize(prefs.getString("basket", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        int total = 0;
        if (basket != null)
            for(Object o_model: basket){
                BagItem model = (BagItem) o_model;
                total = total + Integer.valueOf(model.count) * Integer.valueOf(model.price);
            }
        order.amount = total;
        order.discount = 0;
        order.total = total;
        order.delivery_type = "1";

        List<OrderStepItem> objects = new ArrayList<>();

        objects.add(new OrderStepItem(RESERVE_HEADER, "?????????????? ????????????????", false, ITEM_TYPE_HEADER)); //0

        String mode = PreferenceManager.getDefaultSharedPreferences(OrderStep1Activity.this).getString(EXTRA_FROM, FROM_DELIVERY);
        if (mode.equalsIgnoreCase(FROM_DELIVERY)) {
            order.delivery_type = "1";
            objects.add(new OrderStepItem(DELIVERY_TYPE_DELIVERY, "????????????????", true, ITEM_TYPE_NORMAL)); //1
        }
        if (mode.equalsIgnoreCase(FROM_TAKEAWAY)) {
            order.delivery_type = "2";
            objects.add(new OrderStepItem(DELIVERY_TYPE_SELF, "??????????????????", true, ITEM_TYPE_NORMAL));
        }
        if (mode.equalsIgnoreCase(FROM_INCAFE)) {
            order.delivery_type = "3";
            objects.add(new OrderStepItem(DELIVERY_TYPE_PLACE, "??????????????????", true, ITEM_TYPE_NORMAL));
            order.datetime = "";
        }

        order.pay_type = "1";
        order.confirm_type = "1";

        ProfileModel user = ProfileModel.getInstance(OrderStep1Activity.this);

        order.datetime = "";
        order.username = user.firstname + " " + user.lastname;
        order.phone = user.phone;

        order.station_id = PreferenceManager.getDefaultSharedPreferences(OrderStep1Activity.this).getString(STATION_ID, "0");
        order.address = PreferenceManager.getDefaultSharedPreferences(OrderStep1Activity.this).getString(STATION_ADDRESS, "0");
        order.persons = "1";

        objects.add(new OrderStepItem(RESERVE_HEADER, "????????????", false, ITEM_TYPE_HEADER)); //2
        objects.add(new OrderStepItem(PAY_TYPE_CARD, "???????????????????? ??????????", true, ITEM_TYPE_NORMAL)); //3

        if (mode.equalsIgnoreCase(FROM_DELIVERY)) {
            objects.add(new OrderStepItem(PAY_TYPE_CASH, "?????????????????? ??????????????", false, ITEM_TYPE_NORMAL));//4
            objects.add(new OrderStepItem(PAY_TYPE_CARD_COUR, "???????????? ??????????????", false, ITEM_TYPE_NORMAL));//5
        } else {
            objects.add(new OrderStepItem(PAY_TYPE_CASH, "??????????????????", false, ITEM_TYPE_NORMAL));//4
            objects.add(new OrderStepItem(PAY_TYPE_CARD_COUR, "???????????? ?? ??????????????????", false, ITEM_TYPE_NORMAL));//5
        }

        objects.add(new OrderStepItem(RESERVE_HEADER, "??????????????????????????", false, ITEM_TYPE_HEADER));//6
        objects.add(new OrderStepItem(ACCEPTION_OPERATOR, "???????????? ??????????????????", true, ITEM_TYPE_NORMAL));//7
        objects.add(new OrderStepItem(ACCEPTION_SMS, "???? ??????", false, ITEM_TYPE_NORMAL));//8

        objects.add(new OrderStepItem(RESERVE_HEADER, "???????????????? ??????????", false, ITEM_TYPE_HEADER)); //9
        objects.add(new OrderStepItem(RESERVE_DATETIME, "???? ??????????????", "??????????????", ITEM_TYPE_KEY_VALUE)); //10
        objects.add(new OrderStepItem(RESERVE_HEADER, "???????????????? ???????????????????? ????????????", false, ITEM_TYPE_HEADER)); //11
        objects.add(new OrderStepItem(RESERVE_PERSONS_COUNT, "???????????????????? ????????????", "1", false,  ITEM_TYPE_CHANGER)); //12
        objects.add(new OrderStepItem(RESERVE_HEADER, "?????? ?? ?????? ?????????????????????", false, ITEM_TYPE_HEADER)); //13
        objects.add(new OrderStepItem(RESERVE_USERNAME, "??????", user.firstname + " " + user.lastname, false,  ITEM_TYPE_EDIT)); //14
        objects.add(new OrderStepItem(RESERVE_PHONE, "??????????????", "+" + user.phone, false,  ITEM_TYPE_EDIT)); //15
        objects.add(new OrderStepItem(RESERVE_DESCRIPTION, "???????????????????????????? ????????????????????", "", false, ITEM_TYPE_EDIT)); //16



        balance = Integer.valueOf(ProfileModel.getInstance(this).balance);
        if (order.amount/2 > balance) can_use = balance; else can_use = order.amount/2;
        if (can_use > balance) can_use = balance;
        if (can_use > 0) {
            objects.add(new OrderStepItem(RESERVE_HEADER, "????????????", false, ITEM_TYPE_HEADER)); //17
            objects.add(new OrderStepItem(DISCOUNT_TYPE_NONE, "???? ????????????????????????", true, ITEM_TYPE_NORMAL)); //18
            objects.add(new OrderStepItem(DISCOUNT_TYPE_USE, String.valueOf(can_use) + " ????????????", false, ITEM_TYPE_NORMAL));//19
        }

        mListener = new ReservationProfileActivity.InteractionListener() {
            @Override
            public void InteractionTypeEdit(OrderStepItem object, String value) {
                if (object.id == RESERVE_USERNAME) {
                    order.username = value;
                    objects.set(14, new OrderStepItem(RESERVE_USERNAME, "??????", value, false,  ITEM_TYPE_EDIT));
                }
                if (object.id == RESERVE_PHONE) {
                    order.phone = value;
                    objects.set(15, new OrderStepItem(RESERVE_PHONE, "??????????????", value, false,  ITEM_TYPE_EDIT));
                }
                if (object.id == RESERVE_DESCRIPTION) {
                    order.info = order.info + value;
                    objects.set(16, new OrderStepItem(RESERVE_DESCRIPTION, "???????????????????????????? ????????????????????", value, false, ITEM_TYPE_EDIT));
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
                    objects.set(12, new OrderStepItem(RESERVE_PERSONS_COUNT, "???????????????????? ????????????", value, false,  ITEM_TYPE_CHANGER));
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
                RangeTimePickerDialog picker = new RangeTimePickerDialog(OrderStep1Activity.this, R.style.AppCompatAlertDialogStyle,
                        (view, hourOfDay, minute) -> {
                            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
                            String today = sdf.format(new Date());
                            String selectedTime = addNull(hourOfDay) + ":" + addNull(minute);
                            order.datetime = today + " " + addNull(hourOfDay) + ":" + addNull(minute)+":00";
                            Log.d("Reservation", order.datetime);
                            objects.set(10, new OrderStepItem(RESERVE_DATETIME, selectedTime, "????????????????", ITEM_TYPE_KEY_VALUE));
                            try {
                                adapter.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
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
                if (object.id == PAY_TYPE_CARD) {
                    objects.set(3, new OrderStepItem(PAY_TYPE_CARD, "???????????????????? ??????????", true, ITEM_TYPE_NORMAL));
                    objects.set(4, new OrderStepItem(PAY_TYPE_CASH, "?????????????????? ??????????????", false, ITEM_TYPE_NORMAL));
                    objects.set(5, new OrderStepItem(PAY_TYPE_CARD_COUR, "???????????? ??????????????", false, ITEM_TYPE_NORMAL));
                    order.pay_type = "1";
                }
                if (object.id == PAY_TYPE_CASH) {
                    objects.set(3, new OrderStepItem(PAY_TYPE_CARD, "???????????????????? ??????????", false, ITEM_TYPE_NORMAL));
                    objects.set(4, new OrderStepItem(PAY_TYPE_CASH, "?????????????????? ??????????????", true, ITEM_TYPE_NORMAL));
                    objects.set(5, new OrderStepItem(PAY_TYPE_CARD_COUR, "???????????? ??????????????", false, ITEM_TYPE_NORMAL));
                    order.pay_type = "2";
                }
                if (object.id == PAY_TYPE_CARD_COUR) {
                    objects.set(3, new OrderStepItem(PAY_TYPE_CARD, "???????????????????? ??????????", false, ITEM_TYPE_NORMAL));
                    objects.set(4, new OrderStepItem(PAY_TYPE_CASH, "?????????????????? ??????????????", false, ITEM_TYPE_NORMAL));
                    objects.set(5, new OrderStepItem(PAY_TYPE_CARD_COUR, "???????????? ??????????????", true, ITEM_TYPE_NORMAL));
                    order.pay_type = "3";
                }
                if (object.id == ACCEPTION_OPERATOR) {
                    objects.set(7, new OrderStepItem(ACCEPTION_OPERATOR, "???????????? ??????????????????", true, ITEM_TYPE_NORMAL));
                    objects.set(8, new OrderStepItem(ACCEPTION_SMS, "???? ??????", false, ITEM_TYPE_NORMAL));
                    order.confirm_type = "1";
                }
                if (object.id == ACCEPTION_SMS) {
                    objects.set(7, new OrderStepItem(ACCEPTION_OPERATOR, "???????????? ??????????????????", false, ITEM_TYPE_NORMAL));
                    objects.set(8, new OrderStepItem(ACCEPTION_SMS, "???? ??????", true, ITEM_TYPE_NORMAL));
                    order.confirm_type = "2";
                }

                if (object.id == DISCOUNT_TYPE_NONE) {
                    objects.set(18, new OrderStepItem(DISCOUNT_TYPE_NONE, "???? ????????????????????????", true, ITEM_TYPE_NORMAL));
                    objects.set(19, new OrderStepItem(DISCOUNT_TYPE_USE, String.valueOf(can_use)+" ????????????", false, ITEM_TYPE_NORMAL));
                    order.discount = 0;
                    order.total = order.amount;
                }

                if (object.id == DISCOUNT_TYPE_USE) {
                    objects.set(18, new OrderStepItem(DISCOUNT_TYPE_NONE, "???? ????????????????????????", false, ITEM_TYPE_NORMAL));
                    objects.set(19, new OrderStepItem(DISCOUNT_TYPE_USE, String.valueOf(can_use)+" ????????????", true, ITEM_TYPE_NORMAL));
                    order.discount = can_use;
                    order.total = order.amount - order.discount;
                }

                try {
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {

                }
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
            if (check(order, rootView)) {
                Intent intent = new Intent(OrderStep1Activity.this, WebViewActivity.class);
                intent.putExtra(OrderStep1Activity.EXTRA_ORDER, Parcels.wrap(order));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    boolean check(Order order, View view) {
        if (order.username.isEmpty()) {
            ApplicationController.getInstance().showMessage(view, "?????????????? ???????? ??????");
            return false;
        }
        if (order.phone.isEmpty()) {
            ApplicationController.getInstance().showMessage(view, "?????????????? ?????? ??????????????");
            return false;
        }

        if (order.datetime.isEmpty()) {
            ApplicationController.getInstance().showMessage(view, "?????????????? ??????????");
            return false;
        }


        return true;
    }

}
