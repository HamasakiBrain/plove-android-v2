package com.octarine.plove.pay;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.octarine.plove.MainActivity;
import com.octarine.plove.R;

public class QRWebViewActivity extends AppCompatActivity {

    RelativeLayout rootView;
    WebView webView;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        url = getIntent().getExtras().getString("url");

        rootView = findViewById(R.id.content_web_view);
        webView = findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                Log.d("Замечен редирект", url);
                if ((url.equalsIgnoreCase("http://plove.local/payment-failure?status=fail")) || (url.equalsIgnoreCase("http://plove.local/payment-failure?status=fail"))) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QRWebViewActivity.this);
                    builder.setTitle("Оплата");
                    builder.setMessage("Оплата прошла успешно! Спасибо!");
                    builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                    return true;
                } else if (url.equalsIgnoreCase("http://plove.local/payment-success?status=ok")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QRWebViewActivity.this);
                    builder.setTitle("Оплата");
                    builder.setMessage("Произошла ошибка во время оплаты. Попробуйте еще раз.");
                    builder.setPositiveButton("Ok", (dialogInterface, i) -> {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.plove_red));
                    return true;
                } else {
                    return false;
                }
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Отмена оплаты");
        builder.setMessage("Вы хотите прервать оплату?");

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
}
