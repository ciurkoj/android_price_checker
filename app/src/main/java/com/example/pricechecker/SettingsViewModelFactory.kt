package com.example.pricechecker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pricechecker.repository.LocationRepository

class SettingsViewModelFactory(private val repository: LocationRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(repository) as T
    }
}