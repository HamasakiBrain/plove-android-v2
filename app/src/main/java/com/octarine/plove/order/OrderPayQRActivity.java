package com.octarine.plove.order;

import android.content.Intent;
import android.os.Bundle;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.MainActivity;
import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.api.models.OrderStepItem;
import com.octarine.plove.api.models.ProfileModel;
import com.octarine.plove.api.models.QrResponseModel;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.pay.QRWebViewActivity;
import com.octarine.plove.reservation.RecycleStepAdapter;
import com.octarine.plove.reservation.ReservationProfileActivity;
import org.parceler.Parcels;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrderPayQRActivity extends AppCompatActivity {

    public RecycleStepAdapter adapter;

    public static final int ITEM_TYPE_NORMAL = 0;
    public static final int ITEM_TYPE_HEADER = 1;
    public static final int PAY_TYPE_CARD = 4;
    public static final int PAY_TYPE_CASH = 5;
    public static final int PAY_TYPE_CARD_COUR = 6;
    public static final int RESERVE_HEADER = 14;
    public static final int DISCOUNT_TYPE_NONE = 18;
    public static final int DISCOUNT_TYPE_USE = 19;

    public static final String QR_DATA  = "qr_data";
    int balance = 0;
    int can_use = 0;
    String pay_type = "1";
    int total = 0;
    int discount  = 0;

    ReservationProfileActivity.InteractionListener mListener;
    CoordinatorLayout rootView;
    QrResponseModel model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_step1);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        rootView = findViewById(R.id.rootView);

        RecyclerView recyclerView =  findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<OrderStepItem> objects = new ArrayList<>();
        ProfileModel user = ProfileModel.getInstance(OrderPayQRActivity.this);
        model = Parcels.unwrap(getIntent().getParcelableExtra(QR_DATA));

        objects.add(new OrderStepItem(RESERVE_HEADER, "К оплате: " + model.amount + " руб.", false, ITEM_TYPE_HEADER)); //0
        objects.add(new OrderStepItem(RESERVE_HEADER, "Оплата", false, ITEM_TYPE_HEADER)); //1
        objects.add(new OrderStepItem(PAY_TYPE_CARD, "Картой с телефона", true, ITEM_TYPE_NORMAL)); //2
        objects.add(new OrderStepItem(PAY_TYPE_CASH, "Наличными в ресторане", false, ITEM_TYPE_NORMAL));//3
        objects.add(new OrderStepItem(PAY_TYPE_CARD_COUR, "Картой в ресторане", false, ITEM_TYPE_NORMAL));//4


        balance = Integer.valueOf(ProfileModel.getInstance(this).balance);
        if (Integer.valueOf(model.amount) / 2 > balance) { can_use = balance; } else { can_use = Integer.valueOf(model.amount) / 2; }
        if (can_use > balance) can_use = balance;
        if (can_use > 0) {
            objects.add(new OrderStepItem(RESERVE_HEADER, "Скидка", false, ITEM_TYPE_HEADER)); //5
            objects.add(new OrderStepItem(DISCOUNT_TYPE_NONE, "Не использовать", true, ITEM_TYPE_NORMAL)); //6
            objects.add(new OrderStepItem(DISCOUNT_TYPE_USE, String.valueOf(can_use) + " баллов", false, ITEM_TYPE_NORMAL));//7
        }

        discount = 0;
        total = Integer.valueOf(model.amount);

        mListener = new ReservationProfileActivity.InteractionListener() {
            @Override
            public void InteractionTypeEdit(OrderStepItem object, String value) {

            }

            @Override
            public void InteractionTypeChanger(OrderStepItem object, String value) {

            }

            @Override
            public void InteractionTypeKeyValue(OrderStepItem object) {

            }

            @Override
            public void InteractionTypeSelector(OrderStepItem object) {
                if (object.id == PAY_TYPE_CARD) {
                    objects.set(2, new OrderStepItem(PAY_TYPE_CARD, "Картой с телефона", true, ITEM_TYPE_NORMAL));
                    objects.set(3, new OrderStepItem(PAY_TYPE_CASH, "Наличными в ресторане", false, ITEM_TYPE_NORMAL));
                    objects.set(4, new OrderStepItem(PAY_TYPE_CARD_COUR, "Картой в ресторане", false, ITEM_TYPE_NORMAL));
                    pay_type = "1";
                }
                if (object.id == PAY_TYPE_CASH) {
                    objects.set(2, new OrderStepItem(PAY_TYPE_CARD, "Картой с телефона", false, ITEM_TYPE_NORMAL));
                    objects.set(3, new OrderStepItem(PAY_TYPE_CASH, "Наличными в ресторане", true, ITEM_TYPE_NORMAL));
                    objects.set(4, new OrderStepItem(PAY_TYPE_CARD_COUR, "Картой в ресторане", false, ITEM_TYPE_NORMAL));
                    pay_type = "2";
                }
                if (object.id == PAY_TYPE_CARD_COUR) {
                    objects.set(2, new OrderStepItem(PAY_TYPE_CARD, "Картой с телефона", false, ITEM_TYPE_NORMAL));
                    objects.set(3, new OrderStepItem(PAY_TYPE_CASH, "Наличными в ресторане", false, ITEM_TYPE_NORMAL));
                    objects.set(4, new OrderStepItem(PAY_TYPE_CARD_COUR, "Картой в ресторане", true, ITEM_TYPE_NORMAL));
                    pay_type = "3";
                }

                if (object.id == DISCOUNT_TYPE_NONE) {
                    objects.set(6, new OrderStepItem(DISCOUNT_TYPE_NONE, "Не использовать", true, ITEM_TYPE_NORMAL));
                    objects.set(7, new OrderStepItem(DISCOUNT_TYPE_USE, String.valueOf(can_use)+" баллов", false, ITEM_TYPE_NORMAL));
                    discount = 0;
                    total = Integer.valueOf(model.amount);
                }

                if (object.id == DISCOUNT_TYPE_USE) {
                    objects.set(6, new OrderStepItem(DISCOUNT_TYPE_NONE, "Не использовать", false, ITEM_TYPE_NORMAL));
                    objects.set(7, new OrderStepItem(DISCOUNT_TYPE_USE, String.valueOf(can_use)+" баллов", true, ITEM_TYPE_NORMAL));
                    discount = can_use;
                    total = Integer.valueOf(model.amount) - discount;
                }

                try {
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
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
        getMenuInflater().inflate(R.menu.menu_select_cafe, menu);
        return true;
    }

    public void nextAction() {
        ApplicationController.getInstance().showLoading(this);
        ApplicationController.getApi().payQr(AuthModel.getInstance(OrderPayQRActivity.this).sessionId, model.visitId, model.orderId,
                model.stationId, String.valueOf(discount), String.valueOf(total), pay_type).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                if (response.isSuccessful()) {
                    if (response.body().code == 0) {
                        Intent intent = new Intent(OrderPayQRActivity.this, QRWebViewActivity.class);
                        intent.putExtra("url", response.body().message);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayQRActivity.this);
                        builder.setTitle("Сканирование");
                        builder.setMessage(response.body().message);
                        builder.setPositiveButton("OK", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                            Intent intent = new Intent(OrderPayQRActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                    }
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayQRActivity.this);
                    builder.setTitle("Сканирование");
                    builder.setMessage("Ошибка обработки");
                    builder.setPositiveButton("OK", (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                }
                ApplicationController.getInstance().hideLoading();
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                ApplicationController.getInstance().hideLoading();
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayQRActivity.this);
                builder.setTitle("Сканирование");
                builder.setMessage("Ошибка обработки");
                builder.setPositiveButton("OK", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                });
                AlertDialog alert = builder.create();
                alert.show();
                alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_next) {
            nextAction();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
