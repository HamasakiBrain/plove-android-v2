package com.octarine.plove.settings;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.octarine.plove.R;
import com.octarine.plove.api.models.OptionsItem;
import com.octarine.plove.api.models.ProfileModel;

import java.util.ArrayList;
import java.util.List;

public class OptionsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_options, container, false);


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<OptionsItem> objects = new ArrayList<>();

        objects.add(new OptionsItem(0, "Информация", RecycleOptionsAdapter.ITEM_TYPE_HEADER));
        objects.add(new OptionsItem(0, "Телефон: +" + ProfileModel.getInstance(getActivity()).phone, RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(0, "Номер карты: " + ProfileModel.getInstance(getActivity()).cardnumber, RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(0, "Настройки аккаунта", RecycleOptionsAdapter.ITEM_TYPE_HEADER));
        objects.add(new OptionsItem(1, "Профиль", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(2, "Очистить данные", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(4, "Контакты организации", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(5, "Правила оказания услуг", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));
        objects.add(new OptionsItem(3, "Выход", RecycleOptionsAdapter.ITEM_TYPE_NORMAL));

        RecycleOptionsAdapter adapter = new RecycleOptionsAdapter(getActivity(), objects);
        recyclerView.setAdapter(adapter);
        return view;
    }

}
