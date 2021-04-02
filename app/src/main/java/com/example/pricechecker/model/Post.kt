package com.example.pricechecker.model

import android.util.JsonReader
import com.example.pricechecker.GsonRequest
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.squareup.okhttp.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.GET
import java.util.*


data class Post(
    val search_metadata: JsonObject
)

