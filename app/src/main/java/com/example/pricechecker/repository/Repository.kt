package com.example.pricechecker.repository

import android.util.Log
import com.example.pricechecker.api.RetrofitInstance
import com.example.pricechecker.model.SerpApiQuery

import retrofit2.Response

class Repository {


    suspend fun getCustomQuery(query: String, options: Map<String, String>): Response<SerpApiQuery> {
        Log.e("Launch44 ERROR:" ,"MARK 2")
        return RetrofitInstance.api.getCustomQuery(query, options)
    }

}