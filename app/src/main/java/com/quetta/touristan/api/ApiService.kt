package com.quetta.touristan.api

import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import retrofit2.http.GET

interface ApiService {

    @GET("text")
    suspend fun getPlaces(request: FindAutocompletePredictionsRequest): List<FindAutocompletePredictionsResponse>

}