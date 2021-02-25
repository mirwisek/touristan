package com.quetta.touristan.api

import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

class Repository(private val placesClient: PlacesClient) {

    fun getPlaces(request: FindAutocompletePredictionsRequest) = placesClient.findAutocompletePredictions(request)

}