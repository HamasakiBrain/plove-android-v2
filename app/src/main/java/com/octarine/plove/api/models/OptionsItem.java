package com.octarine.plove.api.models;

/**
 * Created by rustam on 06.02.2018.
 */

public class OptionsItem {

    public int id;
    public String title;
    public int objectType;

    public OptionsItem(int id, String title, int objectType) {
        this.id = id;
        this.title = title;
        this.objectType = objectType;
    }

    @Override
    public String toString() {
        return title;
    }
}