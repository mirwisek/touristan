package com.quetta.touristan.api

import com.google.android.libraries.places.api.model.Place
import com.quetta.touristan.model.Places
import retrofit2.Callback

class ApiHelper(private val apiService: ApiService) {


    fun getPlaces(callback: Callback<Places>, location: String?, radius: Int?, type: String? = null) {
        if(location == null)
            apiService.getPlaces().enqueue(callback)
        else
            apiService.getPlaces(location = location, radius = radius).enqueue(callback)
    }
}