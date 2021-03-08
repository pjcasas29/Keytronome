package com.example.keytronome.apis;

import com.example.keytronome.models.Tweets;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface TwitterAPIInterface {


    @FormUrlEncoded
    @POST("oauth2/token")
    Observable<authToken> postCredentials(@Field("grant_type") String grantType);

    @GET("1.1/search/tweets.json")
    Observable<Tweets> getTweets(@Query("q") String tag);

    @POST("oauth/authenticate")
    Observable<LoginResponse> login(@Body LoginRequest request);
}



