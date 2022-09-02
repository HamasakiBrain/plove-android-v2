package com.octarine.plove;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.QrResponseModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.bag.BagActivity;
import com.octarine.plove.cafes.CafesFragment;
import com.octarine.plove.feedback.FeedbackActivity;
import com.octarine.plove.helpers.BottomNavigationViewHelper;
import com.octarine.plove.history.HistoryFragment;
import com.octarine.plove.order.OrderPayQRActivity;
import com.octarine.plove.profile.ProfileFragment;
import com.octarine.plove.settings.OptionsFragment;
import org.parceler.Parcels;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    RelativeLayout coordinator;

    private ProfileFragment mProfileFragment = new ProfileFragment();
    private HistoryFragment mHistoryFragment = new HistoryFragment();
    private CafesFragment mCafesFragment = new CafesFragment();
    private OptionsFragment mOptionsFragment = new OptionsFragment();
    TextView mBagTitle;

    private NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                getFragmentManager().beginTransaction().replace(R.id.content, mProfileFragment).commit();
                return true;
            case R.id.navigation_record:
                getFragmentManager().beginTransaction().replace(R.id.content, mHistoryFragment).commit();
                return true;
            case R.id.navigation_history:
                getFragmentManager().beginTransaction().replace(R.id.content, mCafesFragment).commit();
                return true;
            case R.id.navigation_map:
                getFragmentManager().beginTransaction().replace(R.id.content, mOptionsFragment).commit();
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getFragmentManager().beginTransaction().replace(R.id.content, mProfileFragment).commit();

        BottomNavigationView navigation =  findViewById(R.id.navigation);
        navigation.setOnItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.removeShiftMode(navigation);

        coordinator =  findViewById(R.id.main_content);

        // Корзина на главной
        mBagTitle =  toolbar.findViewById(R.id.textViewBagTitle);
        mBagTitle.setOnClickListener(view -> {
            Intent i = new Intent(this, BagActivity.class);
            startActivity(i);
        });
        ImageView imageViewShop =  toolbar.findViewById(R.id.imageViewShop);
        imageViewShop.setOnClickListener(view -> {
            Intent i = new Intent(this, BagActivity.class);
            startActivity(i);
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        updateBasket();
    }

    public void updateBasket() {
        ApplicationController.getInstance().getBasket(mBagTitle, " руб ");
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(i);
            return true;
        }
        if (id == R.id.action_messenger) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        moveTaskToBack(true);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getExtras().getString("result");
                if (result != null && !result.isEmpty()) {
                    ApplicationController.getInstance().showLoading(this);
                    ApplicationController.getApi().scanQr(AuthModel.getInstance(this).sessionId, result).enqueue(new Callback<QrResponseModel>() {
                        @Override
                        public void onResponse(Call<QrResponseModel> call, Response<QrResponseModel> response) {
                            ApplicationController.getInstance().hideLoading();
                            if (response.isSuccessful()) {
                                if (response.body().code == 0) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                    builder.setTitle("Сканирование");
                                    builder.setMessage(response.body().message);
                                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                        dialogInterface.dismiss();
                                    });
                                    AlertDialog alert = builder.create();
                                    alert.show();
                                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                                } else {
                                    if (response.body().code == 1000) {
                                        Intent intent = new Intent(MainActivity.this, OrderPayQRActivity.class);
                                        intent.putExtra(OrderPayQRActivity.QR_DATA, Parcels.wrap(response.body()));
                                        startActivity(intent);
                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("Сканирование");
                                        builder.setMessage(response.body().message);
                                        builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                            dialogInterface.dismiss();
                                        });
                                        AlertDialog alert = builder.create();
                                        alert.show();
                                        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                                    }
                                }
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                builder.setTitle("Сканирование");
                                builder.setMessage("Ошибка связи с кассой или проблемы на кассе.");
                                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                                    dialogInterface.dismiss();
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                                alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                            }
                        }

                        @Override
                        public void onFailure(Call<QrResponseModel> call, Throwable t) {
                            ApplicationController.getInstance().hideLoading();
                        }
                    });
                }
            }
        }
    }
}
