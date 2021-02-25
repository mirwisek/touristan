package com.quetta.touristan

import com.quetta.touristan.model.Places

sealed class HomeState {
    object Idle: HomeState()
    object Loading: HomeState()
    data class Result(val places: Places?): HomeState()
//    data class Places(val places: List<AutocompletePrediction>): HomeState()
    data class Error(val error: String?): HomeState()

}