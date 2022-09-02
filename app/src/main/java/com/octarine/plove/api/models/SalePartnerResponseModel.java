package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class SalePartnerResponseModel {

    @SerializedName("partner_id")
    @Expose
    public int partnerId;

    @SerializedName("image")
    @Expose
    public String image;

    public boolean isSelected;

}


