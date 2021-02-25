@file:Suppress("UNCHECKED_CAST")

package com.quetta.touristan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quetta.touristan.api.Repository

class HomeViewModelFactory(private val application: TouristanApp): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(Repository(application.placesClient)) as T
        }
        throw IllegalArgumentException("Unknown class (ViewModel)")
    }
}