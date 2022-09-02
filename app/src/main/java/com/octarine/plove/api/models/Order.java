package com.octarine.plove.api.models;

import org.parceler.Parcel;

/**
 * Created by rustam on 07.02.2018.
 */
@Parcel
public class Order {
    public String delivery_type;
    public String pay_type;
    public String confirm_type;
    public String username;
    public String phone;
    public String datetime;
    public String info;
    public String station_id;
    public String persons;
    public String address;

    public String city;
    public String street;
    public String build;
    public String room;
    public String corpus;

    public int amount;
    public int discount;
    public int total;
}
