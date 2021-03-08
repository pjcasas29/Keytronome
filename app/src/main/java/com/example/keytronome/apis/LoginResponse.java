package com.example.keytronome.apis;

import com.example.keytronome.models.User;
import com.google.gson.annotations.SerializedName;

public class LoginResponse {

    public String getAuthToken() {
        return authToken;
    }

    public User getUser() {
        return user;
    }

    public int getStatusCode() {
        return statusCode;
    }

    @SerializedName("status_code")
    int statusCode;

    @SerializedName("auth_token")
    String authToken;

    @SerializedName("user")
    User user;
}
