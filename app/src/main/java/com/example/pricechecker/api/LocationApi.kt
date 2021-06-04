package com.example.pricechecker.api

import com.example.pricechecker.model.Location
import com.google.gson.JsonObject
import org.json.JSONArray
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("locations.json")
    suspend fun getCustomLocation(
        @Query("q") q: String,
        @Query("limit") limit: Int
    ): Response<List<Location>>
}


