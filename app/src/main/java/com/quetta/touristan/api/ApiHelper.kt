package com.quetta.touristan.api

import com.google.android.libraries.places.api.model.Place
import com.quetta.touristan.model.Places
import retrofit2.Callback

class ApiHelper(private val apiService: ApiService) {

    fun getPhoto(callback: Callback<Places>, type: String? = null) {
        apiService.getPlaces().enqueue(callback)
    }

    fun getPlaces(callback: Callback<Places>, type: String? = null) {
        apiService.getPlaces().enqueue(callback)
    }
}