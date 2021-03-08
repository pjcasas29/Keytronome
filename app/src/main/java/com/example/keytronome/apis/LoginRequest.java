package com.example.keytronome.apis;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("email")
    String email;

    @SerializedName("password")
    String password;

    public LoginRequest(String email, String password){
        this.email = email;
        this.password = password;
    }
}