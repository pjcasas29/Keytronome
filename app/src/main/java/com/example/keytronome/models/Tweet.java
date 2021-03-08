package com.example.keytronome.models;

import com.google.gson.annotations.SerializedName;

public class Tweet {

    @SerializedName("user")
    User user;

    @SerializedName("created_at")
    String date;

    public User getUsername() {
        return user;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    @SerializedName("text")
    String content;
}
