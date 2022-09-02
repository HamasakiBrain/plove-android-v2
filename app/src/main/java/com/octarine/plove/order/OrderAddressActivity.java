package com.octarine.plove.order;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.octarine.plove.R;
import com.octarine.plove.api.models.Order;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.reservation.RecycleStepAdapter;

import org.parceler.Parcels;


public class OrderAddressActivity extends AppCompatActivity {

    public RecycleStepAdapter adapter;

    public static final int ITEM_TYPE_HEADER = 1;

    Order order;
    CoordinatorLayout rootView;
    AutoCompleteBuildAdapter adapterBuild;
    AutoCompleteStreetAdapter adapterStreet;
    EditText textViewRoom, textViewPod, textViewComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_address);

        rootView = findViewById(R.id.rootView);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        order = new Order();

        ProfileModel user = ProfileModel.getInstance(OrderAddressActivity.this);
        order.username = user.firstname + " " + user.lastname;
        order.phone = user.phone;
        order.city = "Казань";
        order.street = "";
        order.build = "";
        order.room = "";
        order.corpus = "";

        int layout = android.R.layout.simple_list_item_1;

        textViewRoom = findViewById (R.id.editTextRoom);
        textViewPod = findViewById (R.id.editTextPod);
        textViewComment = findViewById (R.id.editTextComment);

        AutoCompleteTextView textViewBuild = findViewById (R.id.editTextBuild);
        adapterBuild = new AutoCompleteBuildAdapter(this, layout);
        textViewBuild.setAdapter(adapterBuild);
        textViewBuild.setOnItemClickListener((parent, arg1, pos, id) -> {
            order.build = textViewBuild.getText().toString();
            textViewRoom.setEnabled(true);
            textViewPod.setEnabled(true);
            textViewComment.setEnabled(true);
        });


        AutoCompleteTextView textViewStreet = findViewById (R.id.editTextStreet);
        adapterStreet = new AutoCompleteStreetAdapter(this, layout);
        textViewStreet.setAdapter(adapterStreet);
        textViewStreet.setOnItemClickListener((parent, arg1, pos, id) -> {
            order.street = textViewStreet.getText().toString();
            adapterBuild.setStreetId(adapterStreet.getItemMyId(pos));
            textViewBuild.setEnabled(true);
        });

        textViewStreet.setOnKeyListener((v, keyCode, event) -> {
            textViewBuild.setEnabled(false);
            textViewBuild.setText("");
            order.build = "";
            return false;
        });

        textViewStreet.setOnKeyListener((v, keyCode, event) -> {
            textViewBuild.setEnabled(false);
            textViewBuild.setText("");
            order.build = "";
            return false;
        });

        textViewBuild.setOnKeyListener((v, keyCode, event) -> {

            textViewRoom.setEnabled(false);
            textViewPod.setEnabled(false);

            return false;
        });

        textViewBuild.setEnabled(false);
        textViewRoom.setEnabled(false);
        textViewPod.setEnabled(false);
        textViewComment.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_cafe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_next) {
            if (check(order, rootView)) {
                Intent i = new Intent(OrderAddressActivity.this, OrderStep1Activity.class);
                i.putExtra(OrderStep1Activity.EXTRA_ORDER, Parcels.wrap(order));
                startActivity(i);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean check(Order order, View view) {

        order.room = textViewRoom.getText().toString();
        order.corpus = textViewPod.getText().toString();
        order.info = textViewComment.getText().toString();

        if (order.city.isEmpty()) {
            ApplicationController.getInstance().showMessage(view, "Введите название города");
            return false;
        }
        if (order.street.isEmpty()) {
            ApplicationController.getInstance().showMessage(view, "Введите название улицы");
            return false;
        }
        if (order.build.isEmpty()) {
            ApplicationController.getInstance().showMessage(view, "Введите номер дома");
            return false;
        }

        if (order.room.isEmpty()) {
            ApplicationController.getInstance().showMessage(view, "Введите номер квартиры");
            return false;
        }

        return true;
    }

}
