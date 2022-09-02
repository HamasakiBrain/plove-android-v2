package com.octarine.plove.api.models;

/**
 * Created by rustam on 06.02.2018.
 */
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class MenuModel {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("crm_id")
    @Expose
    public String crm_id;

    @SerializedName("display_name")
    @Expose
    public String displayName;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("preview")
    @Expose
    public String preview;

    @SerializedName("calories")
    @Expose
    public String calories;

    @SerializedName("proteins")
    @Expose
    public String proteins;

    @SerializedName("fats")
    @Expose
    public String fats;

    @SerializedName("carbohydrates")
    @Expose
    public String carbohydrates;

    @SerializedName("weight")
    @Expose
    public String weight;

    @SerializedName("cook_mins")
    @Expose
    public String cook_mins;

    @SerializedName("image")
    @Expose
    public Image image;

    @SerializedName("detail_image")
    @Expose
    public Image detailImage;

    @Parcel
    public static class Image {

        @SerializedName("size1x")
        @Expose
        public String size1x;

        @SerializedName("size2x")
        @Expose
        public String size2x;

        @SerializedName("size3x")
        @Expose
        public String size3x;

        @SerializedName("android")
        @Expose
        public String android;

    }

}

