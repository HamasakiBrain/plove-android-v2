package com.octarine.plove.settings;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;
import com.octarine.plove.api.models.OptionsItem;

import java.util.ArrayList;
import java.util.List;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<OptionsItem> objects = new ArrayList<>();

        objects.add(new OptionsItem(0, "Настройки аккаунта", RecycleOptionsAdapter.ITEM_TYPE_HEADER));
        objects.add(new OptionsItem(1, "Профиль", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(2, "Очистить данные", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(4, "Контакты организации", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(5, "Правила оказания услуг", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(3, "Выход", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));

        RecycleOptionsAdapter adapter = new RecycleOptionsAdapter(this, objects);
        recyclerView.setAdapter(adapter);
    }

}
