package com.example.pricechecker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActViewModel : ViewModel() {


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