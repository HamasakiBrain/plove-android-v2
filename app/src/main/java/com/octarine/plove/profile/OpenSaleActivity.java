package com.octarine.plove.profile;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.octarine.plove.R;
import com.octarine.plove.api.models.NewsResponseModel;

import org.parceler.Parcels;


public class OpenSaleActivity extends AppCompatActivity {

    public static final String EXTRA_CAFE = "sale";
    NewsResponseModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_sale);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        model =  Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CAFE));

        TextView tvDetail =  findViewById(R.id.textViewDetail);
        tvDetail.setText(model.description);

        TextView tvTitleBlack = findViewById(R.id.textViewSaleTitle1);
        tvTitleBlack.setText(model.displayName);

        ImageView cafeImageView = findViewById(R.id.imageViewSaleDetail);
        if (model.detailImage.android != null) {
            Glide.with(this)
                    .load(model.detailImage.android)
                    .crossFade()
                    .placeholder(R.drawable.big)
                    .into(cafeImageView);
        }

    }


}
