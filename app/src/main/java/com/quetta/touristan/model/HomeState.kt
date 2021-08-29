package com.quetta.touristan.model

sealed class HomeState {
    object Idle: HomeState()
    object Loading: HomeState()
    data class Result(val places: List<PlaceItem>?): HomeState()
//    data class Places(val places: List<AutocompletePrediction>): HomeState()
    data class Error(val error: String?): HomeState()

}