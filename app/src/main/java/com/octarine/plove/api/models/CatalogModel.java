package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class CatalogModel {

    @SerializedName("crm_id")
    @Expose
    public String crmId;

    @SerializedName("display_name")
    @Expose
    public String displayName;

    @SerializedName("image_android")
    @Expose
    public String image;

}


