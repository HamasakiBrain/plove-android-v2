package com.octarine.plove.pay;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.octarine.plove.R;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String url = getIntent().getExtras().getString("url");
        String title = getIntent().getExtras().getString("title");
        WebView web = (WebView) findViewById(R.id.webhelp);
        web.loadUrl(url);
        setTitle(title);
    }

}
