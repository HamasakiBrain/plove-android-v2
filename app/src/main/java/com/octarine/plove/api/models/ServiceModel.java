package com.octarine.plove.api.models;

/**
 * Created by rustam on 19.07.17.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by shamtay on 17.05.17.
 */

@Parcel
public class ServiceModel {

    @SerializedName("display_name")
    @Expose
    public String displayName;
    @SerializedName("service_group_id")
    @Expose
    public Integer serviceGroupId;
    @SerializedName("image")
    @Expose
    public String image;
    @SerializedName("services")
    @Expose
    public List<Service> services = null;

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
