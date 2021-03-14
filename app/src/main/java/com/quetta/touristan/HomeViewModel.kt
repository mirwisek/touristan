package com.quetta.touristan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quetta.touristan.api.Repository
import com.quetta.touristan.api.RetrofitBuilder
import com.quetta.touristan.model.PlacePhoto
import com.quetta.touristan.model.Places
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel(private val repository: Repository) : ViewModel() {

    companion object {
        const val COUNTRY_CODE = "PK"
    }

    val placesIntent = Channel<HomeIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)

    val state: StateFlow<HomeState> get() = _state

    private var selectedChip: String? = null

    fun handleIntent(location: String?, radius: Int?, type: String) {
        viewModelScope.launch {
            placesIntent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.GetPlaces -> {
                        if(selectedChip == null) {
                            getPlaces(location, radius, type)
                            selectedChip = type
                        } else {
                            // Only call if the values aren't the same, we want to reduce API cost
                            if(type != selectedChip)
                                getPlaces(location, radius, type)
                        }
                    }
                }
            }
        }
    }


//    private fun getPlaces() {
//
//        viewModelScope.launch {
//            _state.value = HomeState.Loading
//
//            val request = FindAutocompletePredictionsRequest.builder()
//                .setCountries(COUNTRY_CODE)
//                .setQuery("Tourist site in quetta")
//                .build()
//
//            repository.getPlaces(request).addOnSuccessListener { response ->
//                log("Success size is " + response.autocompletePredictions.size)
//                _state.value = HomeState.Places(response.autocompletePredictions)
//            }.addOnFailureListener { e ->
//                log("Success size is " + e.message)
//                _state.value = HomeState.Error(e.message)
//                e.printStackTrace()
//            }
//        }
//    }

    private fun getPlaces(location: String?, radius: Int?, type: String) {

        viewModelScope.launch {
            _state.value = HomeState.Loading

            val callback = object: Callback<Places> {

                override fun onResponse(call: Call<Places>, response: Response<Places>) {
                    if(response.isSuccessful) {
                        log("Success size is " + response.body()?.results?.size)
                        _state.value = HomeState.Result(response.body())
                    } else {
                        log("Unsuccessful: " + response.message())
                        _state.value = HomeState.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<Places>, t: Throwable) {
                    log("Error: " + t.message)
                    _state.value = HomeState.Error(t.message)
                    t.printStackTrace()
                }

            }

            repository.getPlacesApi(callback, location, radius, type)
        }
    }

    fun loadImage(photo: PlacePhoto): String {
        return "${BuildConfig.BASE_API_URL}maps/api/place/photo?key=${RetrofitBuilder.key}&photoreference=${photo.reference}&maxwidth=600"
    }

}