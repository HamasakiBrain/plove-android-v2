package com.octarine.plove.cafes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.octarine.plove.R;
import com.octarine.plove.api.models.StationModel;
import com.octarine.plove.reservation.ReservationProfileActivity;
import org.parceler.Parcels;

public class OpenCafeActivity extends AppCompatActivity {

    public static final String EXTRA_CAFE = "cafe";
    StationModel cafe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_cafe);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cafe = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_CAFE));

        ((TextView) findViewById(R.id.textViewDetail)).setText(cafe.description);
        ((TextView) findViewById(R.id.textViewCafeTitle)).setText(cafe.displayName);
        ((TextView) findViewById(R.id.textViewCafeRegim)).setText(cafe.work_time);

        Glide.with(this)
                .load(cafe.detailImage.android)
                
                .fitCenter()
                .placeholder(R.drawable.big)
                .into((ImageView) findViewById(R.id.imageViewCafeDetail));

        findViewById(R.id.buttonBrone).setOnClickListener(view -> {
            Intent i = new Intent(OpenCafeActivity.this, ReservationProfileActivity.class);
            i.putExtra(OpenCafeActivity.EXTRA_CAFE, Parcels.wrap(cafe));
            startActivity(i);
        });

        findViewById(R.id.buttonMap).setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse( String.format("http://maps.google.com/maps?daddr=%s,%s", cafe.location.latitude, cafe.location.longitude)));
            startActivity(intent);
        });
    }
}
