@file:Suppress("UNCHECKED_CAST")

package com.quetta.touristan.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quetta.touristan.TouristanApp
import com.quetta.touristan.api.Repository

class HomeViewModelFactory(private val application: TouristanApp): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val repository = Repository()
            return HomeViewModel(application, repository) as T
        }
        throw IllegalArgumentException("Unknown class (ViewModel)")
    }
}