package com.octarine.plove.api.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by rustam on 29.06.17.
 */

public class AuthResponseModel {

    @SerializedName("code")
    public Integer code;

    @SerializedName("message")
    public String message;

    @SerializedName("session_id")
    public String sessionId;

    public AuthModel getAuthModel() {
        AuthModel authModel = new AuthModel();
        authModel.sessionId = sessionId;
        return authModel;
    }

}
