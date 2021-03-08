package com.example.keytronome.models;

import com.example.keytronome.models.Tweet;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Tweets {
    public List<Tweet> getTweets() {
        return tweets;
    }

    @SerializedName("statuses")
    private List<Tweet> tweets;
}
