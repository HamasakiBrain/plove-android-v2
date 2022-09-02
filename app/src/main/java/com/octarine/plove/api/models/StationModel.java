package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class StationModel {

    @SerializedName("station_id")
    @Expose
    public String stationId;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("work_time")
    @Expose
    public String work_time;
    @SerializedName("display_name")
    @Expose
    public String displayName;
    @SerializedName("geo")
    @Expose
    public Location location;
    @SerializedName("services")
    @Expose
    public List<Service> services = null;

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
    @Parcel
    public static class Location {

        @SerializedName("latitude")
        @Expose
        public String latitude;
        @SerializedName("longitude")
        @Expose
        public String longitude;

    }
    @Parcel
    public static class Service {

        @SerializedName("display_name")
        @Expose
        public String displayName;
        @SerializedName("service_id")
        @Expose
        public Integer serviceId;
        @SerializedName("service_group_id")
        @Expose
        public Integer serviceGroupId;
        @SerializedName("image")
        @Expose
        public String image;

    }


}