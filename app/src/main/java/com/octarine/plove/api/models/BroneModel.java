package com.octarine.plove.api.models;

import org.parceler.Parcel;

import java.io.Serializable;

/**
 * Created by rustam on 06.02.2018.
 */
@Parcel
public class BroneModel implements Serializable {

    public String username;
    public String phone;
    public String datetime;
    public String info;
    public String station_id;
    public String persons;
    public String address;
}

