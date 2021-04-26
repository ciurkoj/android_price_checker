package com.example.pricechecker

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricechecker.fragments.Item
import com.example.pricechecker.model.SerpApiQuery
import com.example.pricechecker.model.ShoppingResults
import com.example.pricechecker.repository.Repository
import com.example.pricechecker.ui.login.LoginResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.JsonObject
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Response
import java.net.URL

class MainActivityViewModel(private val repository: Repository) : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<Item>()
    val selectedItem: LiveData<Item> get() = mutableSelectedItem

    fun selectItem(item: Item) {
        mutableSelectedItem.value = item
    }
    val myResponse: MutableLiveData<Response<JsonObject>> = MutableLiveData()
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
    private val _currentUser = MutableLiveData<FirebaseUser>()
    val currentUser: Uri? = Firebase.auth.currentUser.photoUrl



}