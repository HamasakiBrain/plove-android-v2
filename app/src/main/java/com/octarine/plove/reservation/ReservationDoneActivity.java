package com.octarine.plove.reservation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.octarine.plove.MainActivity;
import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.api.models.BroneModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.catalog.CategoryActivity;
import com.octarine.plove.common.ObjectSerializer;

import org.parceler.Parcels;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReservationDoneActivity extends AppCompatActivity {

    RelativeLayout rootView;
    public static final String EXTRA_BRONE = "brone";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_done);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        rootView = (RelativeLayout) findViewById(R.id.content_reservation_done);

        BroneModel brone = (BroneModel) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_BRONE));

        TextView address = (TextView) findViewById(R.id.textViewAddrees);
        address.setText(brone.address);

        TextView datetime = (TextView) findViewById(R.id.textViewDate);
        datetime.setText(brone.datetime);

        TextView persons = (TextView) findViewById(R.id.textViewPersons);
        persons.setText(brone.persons);

        Button btnMenu = (Button) findViewById(R.id.buttonOrder);
        btnMenu.setOnClickListener(view -> {
            Intent i = new Intent(ReservationDoneActivity.this, CategoryActivity.class);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.putExtra(CategoryActivity.EXTRA_FROM, CategoryActivity.FROM_INCAFE);
            startActivity(i);
        });

        Button btnCancel = (Button) findViewById(R.id.buttonDeleteReserv);
        btnCancel.setOnClickListener(view -> deleteAction(rootView));

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
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void deleteAction(View view)  {
        ApplicationController.getInstance().showLoading(ReservationDoneActivity.this);
        ApplicationController.getApi().cancelBrone(AuthModel.getInstance(ReservationDoneActivity.this).sessionId).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                Log.d("Reservation", response.raw().toString());
                ApplicationController.getInstance().hideLoading();
                if (response.body().code == 0) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

                    SharedPreferences prefs = getSharedPreferences("Application", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    try {
                        editor.putString("brone", ObjectSerializer.serialize(new BroneModel()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    editor.apply();

                } else {
                    ApplicationController.getInstance().showMessage(view, response.body().message);
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                Log.d("Reservation", t.toString());
                ApplicationController.getInstance().hideLoading();
                ApplicationController.showError(ReservationDoneActivity.this, getString(R.string.connection_error));
            }
        });
    }

}
