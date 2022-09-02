package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by rustam on 29.06.17.
 */
@Parcel
public class NewsResponseModel {

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("preview")
    @Expose
    public String preview;

    @SerializedName("display_name")
    @Expose
    public String displayName;


    @SerializedName("image")
    @Expose
    public StationModel.Image image;

    @SerializedName("detail_image")
    @Expose
    public StationModel.Image detailImage;


}