package com.quetta.touristan

import android.app.Application
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

class TouristanApp: Application() {

    lateinit var placesClient: PlacesClient
        private set

    override fun onCreate() {
        super.onCreate()

        Places.initialize(this, getString(R.string.google_maps_key))
        placesClient = Places.createClient(this)
    }

}