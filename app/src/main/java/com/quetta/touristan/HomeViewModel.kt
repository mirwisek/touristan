package com.quetta.touristan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.quetta.touristan.api.Repository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        const val COUNTRY_CODE = "PK"
    }

    val placesIntent = Channel<HomeIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)

    val state: StateFlow<HomeState> get() = _state


    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            placesIntent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.GetPlaces -> getPlaces()
                }
            }
        }
    }

    private fun getPlaces() {

        viewModelScope.launch {
            _state.value = HomeState.Loading

            val request = FindAutocompletePredictionsRequest.builder()
                .setCountries(COUNTRY_CODE)
                .setQuery("Tourist site in quetta")
                .build()

            repository.getPlaces(request).addOnSuccessListener { response ->
                log("Success size is " + response.autocompletePredictions.size)
                _state.value = HomeState.Places(response.autocompletePredictions)
            }.addOnFailureListener { e ->
                log("Success size is " + e.message)
                _state.value = HomeState.Error(e.message)
                e.printStackTrace()
            }
        }
    }

}