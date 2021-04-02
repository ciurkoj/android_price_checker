package com.example.pricechecker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pricechecker.model.Post
import com.example.pricechecker.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainActivityViewModel(private val repository: Repository) : ViewModel() {

    val myResponse: MutableLiveData<Response<Post>> = MutableLiveData()
    fun getPost(){
        viewModelScope.launch{
            val response = repository.getPost()
            myResponse.value = response
        }
    }
    private val _text = MutableLiveData<String>().apply {
        value = "This is Home Fragment"
    }
    val text: LiveData<String> = _text
    private val _text1 = MutableLiveData<String>().apply {
        value = "This is Username Fragment"
    }
    val text1: LiveData<String> = _text1
    private val _text2 = MutableLiveData<String>().apply {
        value = "This is Dupa Fragment"
    }
    val text2: LiveData<String> = _text2
}