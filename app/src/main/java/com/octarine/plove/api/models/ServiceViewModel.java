package com.octarine.plove.api.models;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by shamtay on 18.05.17.
 */

@Parcel
public class ServiceViewModel {

    public String id;
    public String name;

    public List<FilterItem> filters;

}
