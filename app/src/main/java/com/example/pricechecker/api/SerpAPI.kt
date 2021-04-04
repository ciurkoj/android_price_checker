package com.example.pricechecker.api

import com.example.pricechecker.model.SerpApiQuery
import com.example.pricechecker.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap


interface SerpAPI {
//    @GET("/search.json?engine=google&q=Milk&api_key=${API_KEY}")
//    suspend fun getPost(): Response<Post>
//
//    @GET("/search.json?engine=google&api_key=${API_KEY}")
//    suspend fun getPost2(
//        @Query("q") query: String
//    ): Response<Post>


//    @GET("/search.json?engine=google&api_key=${API_KEY}")
//    suspend fun getCustomPost2(
//        @Query("q") query: String,
//        @Query("device") device: String,
//        @Query("google_domain") domain: String
//    ): Response<List<Post>>
//    https://serpapi.com/search.json?engine=google&q=Coffee&location=Coventry%2C+England%2C+United+Kingdom&google_domain=google.co.uk&gl=uk&hl=en&device=mobile&api_key=55eb0b1549172ab8e0ef83026fdc56fce225517f8006d98c78766c218f3217aa
    @GET("/search.json?engine=google&api_key=${API_KEY}")
    suspend fun getCustomQuery(
        @Query("q") query: String,
        @QueryMap options: Map<String, String>
    ): Response<SerpApiQuery>


}
