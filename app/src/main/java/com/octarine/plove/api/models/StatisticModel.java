package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class StatisticModel {

    @SerializedName("name")
    @Expose
    public String title;

    @SerializedName("value")
    @Expose
    public String value;

    public int color;

    public class StatLine {
        @SerializedName("statistic_id")
        @Expose
        public String statisticId;

        @SerializedName("title")
        @Expose
        public String title;

        @SerializedName("value")
        @Expose
        public float value;
    }

}
