package com.example.pricechecker.api

import com.example.pricechecker.model.SerpApiQuery
import com.example.pricechecker.util.Constants.Companion.API_KEY
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap



interface SerpAPI {
    @GET("/search.json?engine=google&api_key=${API_KEY}")
    suspend fun getCustomQuery(
        @Query("q") query: String,
        @QueryMap options: Map<String, String>
    ): Response<JsonObject>

}
