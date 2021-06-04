package com.example.pricechecker.api

import com.example.pricechecker.util.Constants
import com.example.pricechecker.util.Constants.Companion.BASE_URL1
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstanceLocation {
    private val retrofitLocation by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL1)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: LocationApi by lazy {
        retrofitLocation.create(LocationApi::class.java)
    }


}
