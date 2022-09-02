package com.octarine.plove.api.models;

import org.parceler.Parcel;


@Parcel
public class FilterItem {
    public int id;
    public String title;
    public String shortTitle;
    public int groupId;
    public boolean selected;
    public String image;

    public static boolean equals(FilterItem filter1, FilterItem filter2) {
        return filter1.groupId == filter2.groupId && filter1.id == filter2.id;
    }

}
