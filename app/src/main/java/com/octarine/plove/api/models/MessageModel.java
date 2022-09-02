package com.octarine.plove.api.models;

/**
 * Created by rustam on 12.07.17.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MessageModel {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("message_type")
    @Expose
    public int messageType;

    @SerializedName("from")
    @Expose
    public int from;

    @SerializedName("message")
    @Expose
    public String message;

    @SerializedName("date_time")
    @Expose
    public String dateTime;

}