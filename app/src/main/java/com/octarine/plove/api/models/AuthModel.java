package com.octarine.plove.api.models;


import android.content.Context;
import android.preference.PreferenceManager;


public class AuthModel {

    public String sessionId;
    private static final String PREF_SESSION_ID = "session";
    private static final String PREF_REGISTER_COMPLETED = "register_completed";


    public static AuthModel getInstance(Context context) {
        AuthModel authModel = new AuthModel();
        authModel.sessionId = PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_SESSION_ID, null);
        return authModel;
    }

    public void save(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SESSION_ID, sessionId)
                .apply();
    }

    public void setRegisterCompleted(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putBoolean(PREF_REGISTER_COMPLETED, true)
                .apply();
    }

    public boolean isRegisterCompleted(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_REGISTER_COMPLETED, false);
    }

    public void clear(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().clear().apply();
    }

}
