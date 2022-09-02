package com.octarine.plove.app;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;
//import android.support.annotation.StringRes;
//import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;
import com.octarine.plove.R;
import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
import com.octarine.plove.api.Api;
import com.octarine.plove.api.models.BagItem;
import com.octarine.plove.api.models.MenuModel;
import com.octarine.plove.common.ObjectSerializer;
import java.io.File;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApplicationController extends Application {

    public static final String TAG = "Application";
    private static final String API_URL = "https://crm.jetflix.ru/";
    private static Api mApi;
    private static ApplicationController sInstance;
    private ProgressDialog progressDialog;



    @Override
    public void onCreate() {
        super.onCreate();
        initApi();
        sInstance = this;
    }

    private void initApi() {
        File httpCacheDirectory = new File(getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        Cache cache = new Cache(httpCacheDirectory, cacheSize);
        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(REWRITE_RESPONSE_INTERCEPTOR)
                .addInterceptor(OFFLINE_INTERCEPTOR)
                .cache(cache)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(API_URL)
                .client(client)
                .build();
        mApi = retrofit.create(Api.class);
    }


    private final Interceptor REWRITE_RESPONSE_INTERCEPTOR = chain -> {
        Response originalResponse = chain.proceed(chain.request());
        String cacheControl = originalResponse.header("Cache-Control");
        if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            return originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=0")
                    .build();
        } else {
            return originalResponse;
        }
    };

    private final Interceptor OFFLINE_INTERCEPTOR = chain -> {
        Request request = chain.request();
        if (!isOnline()) {
            Log.d(TAG, "rewriting request");
            int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return chain.proceed(request);
    };

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static Api getApi() {
        return mApi;
    }

    public static synchronized ApplicationController getInstance() {
        return sInstance;
    }

    public static void showError(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showMessage(View v, @StringRes int msg) {
        Snackbar snackbar = Snackbar
                .make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.plove_red));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }
    
    public void showMessage(View v, String msg) {
        Snackbar snackbar = Snackbar
                .make(v, msg, Snackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(getResources().getColor(R.color.plove_red));
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public void showLoading(Activity activity) {
        try {
            progressDialog = new ProgressDialog(activity, R.style.AppCompatAlertDialogStyle);
            progressDialog.setMessage("Пожалуйста, подождите...");
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(true);
            progressDialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void hideLoading() {
        if (progressDialog != null) {
            progressDialog.hide();
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public void addToBasketBag(BagItem item, TextView title) {

        Log.d(TAG, item.displayName);

        ArrayList basket   = new ArrayList();
        ArrayList basketNew   = new ArrayList();
        SharedPreferences prefs = getSharedPreferences("Application", Context.MODE_PRIVATE);
        try {
            basket = (ArrayList) ObjectSerializer.deserialize(prefs.getString("basket", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = 0;
        boolean found = false;

        if (basket != null)
            for(Object o_model: basket){
                BagItem model = (BagItem) o_model;
                if (model.id.equalsIgnoreCase(item.id)) {
                    model.count = String.valueOf(Integer.valueOf(model.count) + 1);
                    found = true;
                }
                basketNew.add(model);
                total = total + Integer.valueOf(model.count) * Integer.valueOf(model.price);
            }

        if (!found) {
            basketNew.add(item);
            total = total + Integer.valueOf(item.count) * Integer.valueOf(item.price);
        }

        Log.d(TAG, String.valueOf(total));

        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("basket", ObjectSerializer.serialize(basketNew));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();

        if (title != null) {
            title.setText(String.valueOf(total)+" руб ");
        }
    }

    public void removeFromBasketBag(BagItem item, TextView title) {

        ArrayList basket   = new ArrayList();
        ArrayList basketNew   = new ArrayList();
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
                if (model.id.equalsIgnoreCase(item.id)) {
                    model.count = String.valueOf(Integer.valueOf(model.count) - 1);
                    if (Integer.valueOf(model.count) > 0) {
                        basketNew.add(model);
                        total = total + Integer.valueOf(model.count) * Integer.valueOf(model.price);
                    }
                } else {
                    basketNew.add(model);
                    total = total + Integer.valueOf(model.count) * Integer.valueOf(model.price);
                }

            }
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("basket", ObjectSerializer.serialize(basketNew));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
        if (title != null) {
            title.setText(String.valueOf(total)+" руб ");
        }
    }

    public void addToBasket(MenuModel menuItem, TextView title) {

        BagItem item = new BagItem();
        item.count = "1";
        item.id = menuItem.crm_id;
        item.displayName = menuItem.displayName;
        item.image = menuItem.image.android;
        item.price  = menuItem.price;

        Log.d(TAG, item.displayName);

        ArrayList basket   = new ArrayList();
        ArrayList basketNew   = new ArrayList();
        SharedPreferences prefs = getSharedPreferences("Application", Context.MODE_PRIVATE);
        try {
            basket = (ArrayList) ObjectSerializer.deserialize(prefs.getString("basket", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int total = 0;
        boolean found = false;

        if (basket != null)
            for(Object o_model: basket){
                BagItem model = (BagItem) o_model;
                if (model.id.equalsIgnoreCase(item.id)) {
                    model.count = String.valueOf(Integer.valueOf(model.count) + 1);
                    found = true;
                }
                basketNew.add(model);
                total = total + Integer.valueOf(model.count) * Integer.valueOf(model.price);
            }

        if (!found) {
            basketNew.add(item);
            total = total + Integer.valueOf(item.count) * Integer.valueOf(item.price);
        }

        Log.d(TAG, String.valueOf(total));

        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("basket", ObjectSerializer.serialize(basketNew));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();

        if (title != null) {
            title.setText(String.valueOf(total)+" руб ");
        }
    }

    public void removeFromBasket(MenuModel menuItem, TextView title) {

        BagItem item = new BagItem();
        item.count = "1";
        item.id = menuItem.crm_id;
        item.displayName = menuItem.displayName;
        item.image = menuItem.image.android;
        item.price  = menuItem.price;

        ArrayList basket   = new ArrayList();
        ArrayList basketNew   = new ArrayList();
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
            if (model.id.equalsIgnoreCase(item.id)) {
                model.count = String.valueOf(Integer.valueOf(model.count) - 1);
                if (Integer.valueOf(model.count) > 0) {
                    basketNew.add(model);
                    total = total + Integer.valueOf(model.count) * Integer.valueOf(model.price);
                }
            } else {
                basketNew.add(model);
                total = total + Integer.valueOf(model.count) * Integer.valueOf(model.price);
            }

        }
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("basket", ObjectSerializer.serialize(basketNew));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
        if (title != null) {
            title.setText(String.valueOf(total)+" руб ");
        }
    }

    public ArrayList getBasket(TextView title, String s) {
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
        if (title != null) {
            title.setText(String.valueOf(total)+s);
        }

        return basket;
    }

    public void clearBasket() {
        ArrayList basketNew   = new ArrayList();
        SharedPreferences prefs = getSharedPreferences("Application", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        try {
            editor.putString("basket", ObjectSerializer.serialize(basketNew));
        } catch (IOException e) {
            e.printStackTrace();
        }
        editor.apply();
    }

    public BagItem inBasket(String id) {
        ArrayList basket  = new ArrayList();
        SharedPreferences prefs = getSharedPreferences("Application", Context.MODE_PRIVATE);
        try {
            basket = (ArrayList) ObjectSerializer.deserialize(prefs.getString("basket", ObjectSerializer.serialize(new ArrayList())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (basket != null)
            for(Object o_model: basket){
                BagItem model = (BagItem) o_model;
                if (model.id.equalsIgnoreCase(id)) {
                    return model;
                }
            }
        return null;
    }
}