package com.octarine.plove.api.models;

import org.parceler.Parcel;

import java.io.Serializable;

@Parcel
public class BagItem implements Serializable {
    public String id;
    public String displayName;
    public String description;
    public String price;
    public String preview;
    public String image;
    public String detailImage;
    public String count;
}
