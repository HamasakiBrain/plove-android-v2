package com.octarine.plove.api.models;

import android.content.Context;
import android.preference.PreferenceManager;

public class ProfileModel {

    public String sessionId;
    private static final String PREF_SESSION_ID = "session_id";
    public String username;
    private static final String PREF_SESSION_USERNAME = "username";
    public String firstname;
    private static final String PREF_SESSION_FIRSTNAME = "firstname";
    public String lastname;
    private static final String PREF_SESSION_LASTNAME = "lastname";
    public String patronymic;
    private static final String PREF_SESSION_PATRONYMIC = "patronymic";
    public String balance;
    private static final String PREF_SESSION_BALANCE = "balance";
    public String cardnumber;
    private static final String PREF_SESSION_CARDNUMBER = "cardnumber";
    public String image;
    private static final String PREF_SESSION_IMAGE = "image";
    public String email;
    private static final String PREF_SESSION_SEX = "sex";
    public String sex;
    private static final String PREF_SESSION_EMAIL = "email";
    public String phone;
    private static final String PREF_SESSION_PHONE = "phone";
    public String birthday;
    private static final String PREF_SESSION_BIRTHDAY = "birthday";
    public int sms_notify = 1;
    private static final String PREF_SESSION_SMS_NOTIFY = "sms_notify";
    public int email_notify = 1;
    private static final String PREF_SESSION_EMAIL_NOTIFY = "email_notify";
    public int bagCounter = 0;
    private static final String PREF_SESSION_BAG_COUNTER = "bag_counter";
    public String is_virtual;
    private static final String PREF_SESSION_VIRTUAL = "is_virtual";

    public static ProfileModel getInstance(Context context) {
        ProfileModel profileModel = new ProfileModel();
        profileModel.sessionId = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_ID, null);
        profileModel.username = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_USERNAME, null);
        profileModel.firstname = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_FIRSTNAME, null);
        profileModel.lastname = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_LASTNAME, null);
        profileModel.patronymic = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_PATRONYMIC, null);
        profileModel.balance = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_BALANCE, null);
        profileModel.cardnumber = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_CARDNUMBER, null);
        profileModel.image = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_IMAGE, null);
        profileModel.email = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_EMAIL, null);
        profileModel.phone = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_PHONE, null);
        profileModel.birthday = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_BIRTHDAY, null);
        profileModel.sms_notify = PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_SESSION_SMS_NOTIFY, 0);
        profileModel.email_notify = PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_SESSION_EMAIL_NOTIFY, 0);
        profileModel.bagCounter = PreferenceManager.getDefaultSharedPreferences(context).getInt(PREF_SESSION_BAG_COUNTER, 0);
        profileModel.is_virtual = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_VIRTUAL, "0");
        profileModel.sex = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_SEX, "1");
        return profileModel;
    }

    public void save(Context context) {
        //Log.d("Profile", sessionId);
        //Log.d("Profile", firstname);
        //Log.d("Profile", lastname);
        //Log.d("Profile", phone);
        try {
            PreferenceManager.getDefaultSharedPreferences(context)
                    .edit()
                    .putString(PREF_SESSION_ID, sessionId)
                    .putString(PREF_SESSION_USERNAME, username)
                    .putString(PREF_SESSION_FIRSTNAME, firstname)
                    .putString(PREF_SESSION_LASTNAME, lastname)
                    .putString(PREF_SESSION_PATRONYMIC, patronymic)
                    .putString(PREF_SESSION_BALANCE, balance)
                    .putString(PREF_SESSION_CARDNUMBER, cardnumber)
                    .putString(PREF_SESSION_IMAGE, image)
                    .putString(PREF_SESSION_EMAIL, email)
                    .putString(PREF_SESSION_PHONE, phone)
                    .putString(PREF_SESSION_BIRTHDAY, birthday)
                    .putInt(PREF_SESSION_SMS_NOTIFY, sms_notify)
                    .putInt(PREF_SESSION_EMAIL_NOTIFY, email_notify)
                    .putInt(PREF_SESSION_BAG_COUNTER, bagCounter)
                    .putString(PREF_SESSION_VIRTUAL, is_virtual)
                    .putString(PREF_SESSION_SEX, sex)
                    .apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
