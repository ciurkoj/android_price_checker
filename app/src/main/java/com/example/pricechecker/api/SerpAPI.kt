package com.example.pricechecker.api

import com.example.pricechecker.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface SerpAPI {
    @GET("search.json?engine=google&q=Coffee&api_key=55eb0b1549172ab8e0ef83026fdc56fce225517f8006d98c78766c218f3217aa")
    suspend fun getPost(): Response<Post>
}