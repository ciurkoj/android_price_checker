package com.example.pricechecker.repository

import android.util.Log
import com.example.pricechecker.api.RetrofitInstance
import com.example.pricechecker.model.SerpApiQuery
import com.google.gson.JsonObject
import kotlinx.serialization.json.Json
import org.json.JSONObject

import retrofit2.Response

class Repository {


    suspend fun getCustomQuery(query: String, options: Map<String, String>): Response<JsonObject> {
        Log.e("Launch44 ERROR:" ,"MARK 2")
        return RetrofitInstance.api.getCustomQuery(query, options)
    }

}