package com.octarine.plove.pay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.octarine.plove.MainActivity;
import com.octarine.plove.R;
import com.octarine.plove.api.models.AuthModel;
import com.octarine.plove.api.models.AuthResponseModel;
import com.octarine.plove.api.models.BagItem;
import com.octarine.plove.api.models.Order;
import com.octarine.plove.app.ApplicationController;
import com.octarine.plove.order.OrderDoneActivity;

import org.json.JSONException;
import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.octarine.plove.order.OrderStep1Activity.EXTRA_ORDER;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class WebViewActivity extends AppCompatActivity {

    RelativeLayout rootView;
    WebView webView;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        order = (Order) Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_ORDER));

        rootView = (RelativeLayout) findViewById(R.id.content_web_view);
        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                Log.d("Замечен редирект", url);
                if ((url.equalsIgnoreCase("http://plove.local/payment-failure?status=fail")) || (url.equalsIgnoreCase("http://plove.local/payment-failure?status=fail"))) {
                    Intent intent = new Intent(getApplicationContext(), OrderDoneActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("status", 0);
                    startActivity(intent);
                    return true;
                } else if (url.equalsIgnoreCase("http://plove.local/payment-success?status=ok")) {
                    Intent intent = new Intent(getApplicationContext(), OrderDoneActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("status", 1);
                    intent.putExtra("delivery_time", order.datetime);
                    intent.putExtra("persons_count", order.persons);
                    startActivity(intent);
                    return true;
                } else {
                    return false;
                }
            }
        });

        try {
            calcOrder(order);
        } catch (JSONException e) {
            e.printStackTrace();
            Snackbar snackbar = Snackbar
                    .make(rootView, "Неизвестная ошибка... =(", Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(getResources().getColor(R.color.plove_red));
            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Отмена оплаты");
        builder.setMessage("Вы хотите отменить оплату и заказ?");

        builder.setPositiveButton("Да", (dialogInterface, i) -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        builder.setNegativeButton("Нет", (dialogInterface, i) -> {

        });
        AlertDialog alert = builder.create();
        alert.show();
        alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
        alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.plove_red));
    }


    public JsonObject createOrder(Order orderModel) throws JSONException {
        JsonObject order = new JsonObject();
        order.addProperty("session_id", AuthModel.getInstance(WebViewActivity.this).sessionId);

        order.addProperty("city", orderModel.city);
        order.addProperty("street", orderModel.street);
        order.addProperty("build", orderModel.build);
        order.addProperty("room", orderModel.room);
        order.addProperty("corpus", orderModel.corpus);
        order.addProperty("info", orderModel.info);

        order.addProperty("delivery_type", orderModel.delivery_type);
        order.addProperty("pay_type", orderModel.pay_type);
        order.addProperty("confirm_type", orderModel.confirm_type);
        order.addProperty("username", orderModel.username);
        order.addProperty("phone", orderModel.phone);
        order.addProperty("persons", orderModel.persons);
        order.addProperty("datetime", orderModel.datetime);
        order.addProperty("station_id", orderModel.station_id);
        order.addProperty("amount", orderModel.amount);
        order.addProperty("discount", orderModel.discount);
        order.addProperty("total", orderModel.total);

        order.addProperty("reserv_id", "0");

        JsonArray items = new JsonArray();
        ArrayList basket = ApplicationController.getInstance().getBasket(null, "");
        if (basket!=null) {
            for(Object o_model: basket) {
                BagItem model = (BagItem) o_model;
                JsonObject bagItem = new JsonObject();
                bagItem.addProperty("id", model.id);
                bagItem.addProperty("count", model.count);
                items.add(bagItem);
            }
        }

        order.add("items", items);
        Log.d("Order", order.toString());
        return  order;
    }

    public void calcOrder(Order order) throws JSONException {

        final ProgressDialog progressDialog = new ProgressDialog(this, R.style.AppCompatAlertDialogStyle);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Пожалуйста, подождите...");
        progressDialog.show();

        ApplicationController.getApi().order(createOrder(order), AuthModel.getInstance(WebViewActivity.this).sessionId).enqueue(new Callback<AuthResponseModel>() {
            @Override
            public void onResponse(Call<AuthResponseModel> call, Response<AuthResponseModel> response) {
                Log.d("App", response.raw().toString());
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    if (response.body().code == 1000) {
                        webView.stopLoading();
                        webView.loadUrl(response.body().message);
                        Log.d("Redirect", response.body().message);
                    } else {
                        if (response.body().code == 0) {

                            Intent intent = new Intent(getApplicationContext(), OrderDoneActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putExtra("status", 1);
                            intent.putExtra("delivery_time", order.datetime);
                            intent.putExtra("persons_count", order.persons);
                            startActivity(intent);
                        } else {
                            Snackbar snackbar = Snackbar
                                    .make(rootView, response.body().message, Snackbar.LENGTH_LONG);
                            View snackbarView = snackbar.getView();
                            snackbarView.setBackgroundColor(getResources().getColor(R.color.plove_red));
                            TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                            textView.setTextColor(Color.WHITE);
                            snackbar.show();
                        }
                    }
                } else {
                    Snackbar snackbar = Snackbar
                            .make(rootView, R.string.connection_error, Snackbar.LENGTH_LONG);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(getResources().getColor(R.color.plove_red));
                    TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                }
            }

            @Override
            public void onFailure(Call<AuthResponseModel> call, Throwable t) {
                progressDialog.dismiss();
                Snackbar snackbar = Snackbar
                        .make(rootView, R.string.connection_error, Snackbar.LENGTH_LONG);
                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(getResources().getColor(R.color.plove_red));
                TextView textView = (TextView) snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);
                snackbar.show();
            }
        });
    }



}
