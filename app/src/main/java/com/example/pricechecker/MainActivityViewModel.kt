package com.example.pricechecker

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricechecker.model.SerpApiQuery
import com.example.pricechecker.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<SerpApiQuery>> = MutableLiveData()

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

}