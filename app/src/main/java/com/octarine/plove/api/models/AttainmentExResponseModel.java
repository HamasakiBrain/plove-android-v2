package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class AttainmentExResponseModel {

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("items")
    @Expose
    public AttainmentItem[] items;


    @Parcel
    public static class AttainmentItem {

        @SerializedName("id")
        @Expose
        public String id;

        @SerializedName("action")
        @Expose
        public String action = "";

        @SerializedName("description")
        @Expose
        public String description;

        @SerializedName("display_name")
        @Expose
        public String displayName;

        @SerializedName("progress")
        @Expose
        public int progress = 0;

        @SerializedName("subtitle")
        @Expose
        public String subtitle = "";

        @SerializedName("progress_title")
        @Expose
        public String progress_title = "";


        @SerializedName("subtitle_part2")
        @Expose
        public String subtitle_part2 = "";

        @SerializedName("color")
        @Expose
        public String color = "";

        @SerializedName("quantity")
        @Expose
        public int quantity = 0;

        @SerializedName("size")
        @Expose
        public int max = 0;

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

        @SerializedName("active")
        @Expose
        public int active = 0;

    }

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