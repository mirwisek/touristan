package com.quetta.touristan

import com.google.android.libraries.places.api.model.AutocompletePrediction

sealed class HomeState {
    object Idle: HomeState()
    object Loading: HomeState()
    data class Places(val places: List<AutocompletePrediction>): HomeState()
    data class Error(val error: String?): HomeState()

}