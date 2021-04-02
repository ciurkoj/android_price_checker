package com.example.pricechecker.api

import com.example.pricechecker.util.Constants.Companion.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    val api : SerpAPI by lazy {
        retrofit.create(SerpAPI::class.java)

    }
}