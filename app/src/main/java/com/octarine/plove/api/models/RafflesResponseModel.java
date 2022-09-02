package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class RafflesResponseModel {

    @SerializedName("coupon")
    @Expose
    public String coupon;

}