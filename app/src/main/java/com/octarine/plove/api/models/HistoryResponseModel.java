package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rustam on 29.06.17.
 */

public class HistoryResponseModel {

    @SerializedName("offset")
    @Expose
    public Integer offset;
    @SerializedName("limit")
    @Expose
    public Integer limit;
    @SerializedName("history")
    @Expose
    public List<History> history = null;

    public class Position {

        @SerializedName("display_name")
        @Expose
        public String displayName;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("count")
        @Expose
        public String count;
        @SerializedName("total_price")
        @Expose
        public String totalPrice;
    }


    public class History {

        @SerializedName("order_id")
        @Expose
        public String orderId;
        @SerializedName("item_id")
        @Expose
        public String itemId;
        @SerializedName("where")
        @Expose
        public String where;
        @SerializedName("datetime")
        @Expose
        public String datetime;
        @SerializedName("positions")
        @Expose
        public List<Position> positions = null;
        @SerializedName("points_plus")
        @Expose
        public String pointsPlus;
        @SerializedName("points_minus")
        @Expose
        public String pointsMinus;
        @SerializedName("total")
        @Expose
        public String total;

    }


}