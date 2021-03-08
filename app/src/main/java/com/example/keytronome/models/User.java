package com.example.keytronome.models;

import com.google.gson.annotations.SerializedName;

public class User {

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    @SerializedName("name")
    String name;

    @SerializedName("screen_name")
    String screenName;

    @SerializedName("profile_image_url_https")
    String imageUrl;

}

