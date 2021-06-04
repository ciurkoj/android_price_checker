package com.example.pricechecker

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricechecker.model.Location
import com.example.pricechecker.repository.LocationRepository
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import retrofit2.Response

class SettingsViewModel(private val repository: LocationRepository) : ViewModel() {
    var myCustomLocation: MutableLiveData<Response<List<Location>>> = MutableLiveData()

    fun getCustomLocation(q:String, limit:Int) {
        viewModelScope.launch {
            val response = repository.getCustomLocation(q,limit)
            myCustomLocation.value = response
        }
    }
}