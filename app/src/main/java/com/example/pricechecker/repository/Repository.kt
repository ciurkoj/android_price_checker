package com.example.pricechecker.repository

import com.example.pricechecker.api.RetrofitInstance
import com.example.pricechecker.model.Post

import retrofit2.Response

class Repository {

    suspend fun getPost(): Response<Post> {
        return RetrofitInstance.api.getPost()
    }
}