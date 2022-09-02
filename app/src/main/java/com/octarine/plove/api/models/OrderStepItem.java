package com.octarine.plove.api.models;


public  class OrderStepItem {

    public int id;
    public String content;
    public String value;
    public boolean checked;
    public int objectType;
    public int max;

    public OrderStepItem(int id, String content, boolean checked, int objectType) {
        this.id = id;
        this.content = content;
        this.checked = checked;
        this.objectType = objectType;
    }


    // edit
    public OrderStepItem(int id, String content, String value, boolean checked, int objectType) {
        this.id = id;
        this.content = content;
        this.checked = checked;
        this.objectType = objectType;
        this.value = value;
    }

    // key-value
    public OrderStepItem(int id, String content, String value, int objectType) {
        this.id = id;
        this.content = content;
        this.value = value;
        this.checked = false;
        this.objectType = objectType;
    }


    // slider
    public OrderStepItem(int id, String content, String value, int objectType, int max) {
        this.id = id;
        this.content = content;
        this.value = value;
        this.checked = false;
        this.objectType = objectType;
        this.max = max;
    }

    @Override
    public String toString() {
        return content;
    }
}