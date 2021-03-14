package com.quetta.touristan.api

import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.quetta.touristan.model.Places
import retrofit2.Callback

class Repository(
    private val placesClient: PlacesClient,
    private val apiHelper: ApiHelper
) {

    fun getPlaces(request: FindAutocompletePredictionsRequest) =
        placesClient.findAutocompletePredictions(request)

    fun getPlacesApi(callback: Callback<Places>, location: String?, radius: Int?, type: String) =
        apiHelper.getPlaces(location = location, radius = radius, callback = callback, type = type)


}