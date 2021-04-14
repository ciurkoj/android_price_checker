package com.example.pricechecker.fragments;


import com.google.gson.JsonObject;

import java.util.List;
import java.util.Map;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

import static com.example.pricechecker.util.Constants.API_KEY;

public interface JsonPlaceHolderApi {
    @GET("search.json?engine=google&api_key="+ API_KEY)
    Call<JsonObject> getPosts(@QueryMap Map<String, String> parameters);

}