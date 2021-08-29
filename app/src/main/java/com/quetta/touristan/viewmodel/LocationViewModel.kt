package com.quetta.touristan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng

class LocationViewModel: ViewModel()  {

    val currentLocation = MutableLiveData<LatLng>()

}