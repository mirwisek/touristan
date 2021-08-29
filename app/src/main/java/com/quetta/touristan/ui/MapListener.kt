package com.quetta.touristan.ui

import com.quetta.touristan.model.PlaceItem
import com.quetta.touristan.model.TourPlace

interface MapListener {
    fun drawOnMap(placeItem: PlaceItem)
}