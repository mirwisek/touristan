package com.quetta.touristan.api

import com.google.android.libraries.places.api.net.PlacesClient
import com.quetta.touristan.model.PlaceItem

object Singleton {
    private var repository: Repository? = null

    fun getInstance(placesClient: PlacesClient, apiHelper: ApiHelper): Repository {
        if(repository == null) {
            repository = Repository()
//            repository = Repository(placesClient, apiHelper)
        }
        return repository!!
    }
}

class Repository(
//    private val placesClient: PlacesClient,
//    private val apiHelper: ApiHelper
) {

    val cache = hashMapOf<String, List<PlaceItem>>()

//    fun getPlaces(request: FindAutocompletePredictionsRequest) =
//        placesClient.findAutocompletePredictions(request)
//
//    fun getPlacesApi(callback: Callback<Places>, location: String?, radius: Int?, type: String) =
//        apiHelper.getPlaces(location = location, radius = radius, callback = callback, type = type)

    fun storeInCache(key: String, places: List<PlaceItem>?) {
        places?.let {
            cache[key] = it
        }
    }
}