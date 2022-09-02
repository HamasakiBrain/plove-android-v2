package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by rustam on 29.06.17.
 */
@Parcel
public class QrResponseModel {

    /*
    * let code: Int
    let message: String
    let sessionId: String
    let amount: String
    let visitId: String
    let orderId: String
    let stationId: String
    * */

    @SerializedName("code")
    @Expose
    public int code;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("session_id")
    @Expose
    public String sessionId;

    @SerializedName("amount")
    @Expose
    public String amount;

    @SerializedName("visit_id")
    @Expose
    public String visitId;

    @SerializedName("order_id")
    @Expose
    public String orderId;

    @SerializedName("station_id")
    @Expose
    public String stationId;


}