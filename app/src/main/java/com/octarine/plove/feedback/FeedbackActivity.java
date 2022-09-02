package com.octarine.plove.feedback;

import android.content.Intent;
import android.net.Uri;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.Toolbar;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.jivosite.sdk.JivoDelegate;
import com.jivosite.sdk.JivoSdk;
import com.octarine.plove.R;

public class FeedbackActivity extends AppCompatActivity implements JivoDelegate {

    JivoSdk jivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        jivo = new JivoSdk((WebView)findViewById(R.id.webview), "ru");
        jivo.delegate = this;
        jivo.prepare();

        setTitle("Чат с оператором");

    }

    @Override

    public void onEvent(String name, String data) {
        if(name.equals("url.click")){
            if(data.length() > 2){
                String url = data.substring(1, data.length() - 1);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }
        }
    }

}
