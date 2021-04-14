package com.example.pricechecker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricechecker.model.SerpApiQuery
import com.example.pricechecker.model.ShoppingResults
import com.example.pricechecker.repository.Repository
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Response

class MainActivityViewModel(private val repository: Repository) : ViewModel() {

    @kotlin.jvm.JvmField
    var myResponse1: MutableLiveData<Response<JsonObject>> = MutableLiveData()

    //    lateinit var myResponse1: LiveData<Any>
    val myResponse: MutableLiveData<Response<JsonObject>> = MutableLiveData()
//    var  myResponse1: MutableLiveData<Response<JsonObject>> = MutableLiveData()
    fun getCustomQuery(query: String, options: Map<String, String>){
        Log.e("VIEW ERROR:" ,"MARK 2")

        viewModelScope.launch{
            Log.e("Launch ERROR:" ,"MARK 2")

            val response = repository.getCustomQuery(query, options)
            Log.e("Launch1 ERROR:" ,"MARK 2")
            myResponse.value = response

            Log.e("Launch2 ERROR:" ,"MARK 2")
        }
    }
    fun getCustomQuery1(query: String, options: Map<String, String>){
        Log.e("VIEW ERROR:" ,"MARK 2")

        viewModelScope.launch{
            Log.e("Launch ERROR:" ,"MARK 2")

            val response = repository.getCustomQuery(query, options)
            Log.e("Launch1 ERROR:" ,"MARK 2")
            myResponse1.value = response

            Log.e("Launch2 ERROR:" ,"MARK 2")
        }
    }
}