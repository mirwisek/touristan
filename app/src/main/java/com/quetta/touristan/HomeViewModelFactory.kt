@file:Suppress("UNCHECKED_CAST")

package com.quetta.touristan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.quetta.touristan.api.ApiHelper
import com.quetta.touristan.api.ApiService
import com.quetta.touristan.api.Repository
import com.quetta.touristan.api.RetrofitBuilder

class HomeViewModelFactory(private val application: TouristanApp): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            val repository = Repository(application.placesClient, ApiHelper(RetrofitBuilder.apiService))
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown class (ViewModel)")
    }
}