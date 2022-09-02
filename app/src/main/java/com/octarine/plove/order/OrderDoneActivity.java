
package com.octarine.plove.order;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.MainActivity;
import com.octarine.plove.R;
import com.octarine.plove.app.ApplicationController;

import java.util.ArrayList;
import java.util.List;

public class OrderDoneActivity extends AppCompatActivity {

    RecyclerOrderBagAdapter adapter;
    int status;
    String deliveryTime = "0";
    String personsCount = "1";
    public List<Object> mItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_done);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        status = getIntent().getExtras().getInt("status");
        deliveryTime = getIntent().getExtras().getString("delivery_time");
        personsCount = getIntent().getExtras().getString("persons_count");

        TextView textViewDeliveryTime = (TextView) findViewById(R.id.textViewDeliveryTime);
        textViewDeliveryTime.setText("~ " + deliveryTime);
        TextView textViewPersonsCount = (TextView) findViewById(R.id.textViewPersonsCount);
        textViewPersonsCount.setText(personsCount);

        TextView textViewOrderResult = findViewById(R.id.textViewOrderResult);
        RelativeLayout rl1 = findViewById(R.id.rl2);
        RelativeLayout rl2 = (RelativeLayout) findViewById(R.id.rl5);
        RelativeLayout rl3 = (RelativeLayout) findViewById(R.id.rl6);
        RelativeLayout rl4 = (RelativeLayout) findViewById(R.id.rl7);

        if (status == 1) {
            adapter = new RecyclerOrderBagAdapter(this, mItems);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            ApplicationController.getInstance().clearBasket();
        } else {
            textViewOrderResult.setText("Ошибка оплаты заказа. Попробуйте еще раз.");
            rl1.setVisibility(View.INVISIBLE);
            rl2.setVisibility(View.INVISIBLE);
            rl3.setVisibility(View.INVISIBLE);
            rl4.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_reserve, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            //Intent i = new Intent(context, ReservationDoneActivity.class);
            //i.putExtra("cafe_id", cafe.getId());
            //Log.d("CafeList", String.valueOf(cafe.getId()));
            //context.startActivity(i);
            if (status == 1) {
                //RealmController.getInstance().clearBag();
                //ApplicationController.DELIVERY_ADDRESS[ApplicationController.DELIVERY_PERSONS_COUNT] = "1";
            }
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

}
