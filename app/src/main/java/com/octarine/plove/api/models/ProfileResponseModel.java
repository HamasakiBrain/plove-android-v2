package com.octarine.plove.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class ProfileResponseModel {
    @SerializedName("firstname")
    @Expose
    public String firstName;

    @SerializedName("phone")
    @Expose
    public String phone;

    @SerializedName("lastname")
    @Expose
    public String lastName;

    @SerializedName("patronymic")
    @Expose
    public String patronymic;

    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("session_id")
    @Expose
    public String sessionId;
    @SerializedName("balance")
    @Expose
    public String balance;
    @SerializedName("e_balance")
    @Expose
    public String eBalance;
    @SerializedName("sex")
    @Expose
    public String sex;
    @SerializedName("sms_notify")
    @Expose
    public int notifySms;
    @SerializedName("email_notify")
    @Expose
    public int notifyEmail;
    @SerializedName("cardnumber")
    @Expose
    public String cardnumber;
    @SerializedName("birthday")
    @Expose
    public String birthday;
    @SerializedName("avatar")
    @Expose
    public Avatar avatar;
    @SerializedName("is_virtual")
    @Expose
    public String is_virtual;


    public static final String NOTIFY_ON = "2";
    public static final String NOTIFY_OFF = "1";

    @Parcel
    public static class Avatar {
        @SerializedName("size1x")
        @Expose
        public String size1x;
        @SerializedName("size2x")
        @Expose
        public String size2x;
        @SerializedName("size3x")
        @Expose
        public String size3x;
        @SerializedName("android")
        @Expose
        public String android;
    }

    public boolean isCorrect() {
        return !(firstName == null || lastName == null || email == null || birthday == null) && !(firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || birthday.isEmpty());

    }

    public ProfileModel getProfileModel() {
        ProfileModel profileModel = new ProfileModel();
        profileModel.sessionId = sessionId;
        profileModel.username = firstName+" "+lastName;
        profileModel.lastname = lastName;
        profileModel.patronymic = patronymic;
        profileModel.firstname = firstName;
        profileModel.balance = balance;
        profileModel.cardnumber = cardnumber;
        profileModel.phone = phone;
        profileModel.email = email;
        profileModel.sms_notify = notifySms;
        profileModel.email_notify = notifyEmail;
        profileModel.birthday = birthday;
        try {
            profileModel.image = avatar.android;
        } catch (Exception e) {
            profileModel.image = "";
        }
        profileModel.is_virtual = is_virtual;
        return profileModel;
    }

    @Override
    public String toString() {
        return firstName;
    }
}
