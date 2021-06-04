package com.example.pricechecker.repository

import com.example.pricechecker.api.RetrofitInstance
import com.example.pricechecker.api.RetrofitInstanceLocation
import com.example.pricechecker.model.Location
import com.google.gson.JsonObject
import org.json.JSONArray
import retrofit2.Response

class LocationRepository {
    suspend fun getCustomLocation(q : String, limit: Int ): Response<List<Location>> {
        return RetrofitInstanceLocation.api.getCustomLocation(q,limit)
    }
}