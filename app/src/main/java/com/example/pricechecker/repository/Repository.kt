package com.example.pricechecker.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.pricechecker.api.RetrofitInstance
import com.example.pricechecker.fragments.Item
import com.google.gson.JsonObject
import retrofit2.Response

class Repository  constructor() {

    private var recentSearchesList = MutableLiveData<ArrayList<Item>>()

    fun getRecentSearchesList() : MutableLiveData<ArrayList<Item>> {

        return recentSearchesList
    }
    fun addRecentSearchItem( item : Item){
        recentSearchesList.value?.add(item)
        Log.e("sdadSSUCCESS",item.itemTitle)

    }
//    fun setRecentSearchesList(item: Item) : ArrayList<Item> {
//        return recentSearchesList.add(
//            Item(
//                item.itemTitle,
//                item.itemPrice,
//                item.itemSource,
//                item.thumbnailUrl
//            )
//        )
//    }

    private var currentProgress: MutableLiveData<String> = MutableLiveData()

    fun getLiveProgress(): MutableLiveData<String> {
        return currentProgress
    }

    companion object {

        private val mInstance: Repository =
            Repository()

        @Synchronized
        fun getInstance(): Repository {
            return mInstance
        }
    }

    suspend fun getCustomQuery(query: String, options: Map<String, String>): Response<JsonObject> {
        Log.e("Launch44 ERROR:", "MARK 2")
        return RetrofitInstance.api.getCustomQuery(query, options)
    }

}