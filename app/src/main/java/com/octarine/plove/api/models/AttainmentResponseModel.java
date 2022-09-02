package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class AttainmentResponseModel {

    @SerializedName("attainment_id")
    @Expose
    public String attainmentId;

    @SerializedName("attainment_uid")
    @Expose
    public String attainmentUid;


    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("display_name")
    @Expose
    public String displayName;

    @SerializedName("progress")
    @Expose
    public int progress;

    @SerializedName("size")
    @Expose
    public int max;

    @SerializedName("small_icon")
    @Expose
    public Image smallIcon;

    @SerializedName("small_image")
    @Expose
    public Image smallImage;

    @SerializedName("large_icon")
    @Expose
    public Image largeIcon;

    @SerializedName("large_image")
    @Expose
    public Image largeImage;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("quantity")
    @Expose
    public String quantity;

    @SerializedName("is_catalog")
    @Expose
    public boolean isCatalog;

    @SerializedName("is_club")
    @Expose
    public int isClub;

    @SerializedName("is_sale")
    @Expose
    public int isSale;

    @SerializedName("in_club")
    @Expose
    public int inClub;

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