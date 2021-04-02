package com.example.pricechecker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pricechecker.repository.Repository

class MainActivityViewModelFactory(private val repository: Repository): ViewModelProvider.Factory{



    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainActivityViewModel(repository) as T
    }
}