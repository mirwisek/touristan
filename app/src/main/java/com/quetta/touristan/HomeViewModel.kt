package com.quetta.touristan

import androidx.lifecycle.*
import com.quetta.touristan.api.Repository
import com.quetta.touristan.api.RetrofitBuilder
import com.quetta.touristan.model.HomeIntent
import com.quetta.touristan.model.HomeState
import com.quetta.touristan.model.PlacePhoto
import com.quetta.touristan.model.Places
import kotlinx.coroutines.Job
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

    private var selectedCategory: String? = null

    private val cache = hashMapOf<String, Places>()
    var placeType: String = ""

    fun handleIntent(location: String?, radius: Int?, type: String) {
        placeType = type

        viewModelScope.launch {
            placesIntent.consumeAsFlow().collect {
                when (it) {
                    is HomeIntent.GetPlaces -> {
                        if(selectedCategory == null) {
                            getPlaces(location, radius, type)
                            selectedCategory = type
                        } else {
                            // Only call if the values aren't the same, we want to reduce API cost
                            if(type != selectedCategory)
                                getPlaces(location, radius, type)
                        }
                    }
                }
            }
        }
    }

    private fun getPlaces(location: String?, radius: Int?, type: String): Job {

        return viewModelScope.launch {
            _state.value = HomeState.Loading
            // Use cache value if available and not empty
            if(cache.contains(type) && cache[type]!!.results.isNotEmpty()) {
                _state.value = HomeState.Result(cache[type])
            } // Otherwise retrieve fresh list from network
            else {
                val callback = object: Callback<Places> {

                    override fun onResponse(call: Call<Places>, response: Response<Places>) {
                        if(type == placeType) {
                            if(response.isSuccessful) {
                                log("params ${call.request().url.query}")
                                log("Success size is " + response.body()?.results?.size)
                                // Store in cache for later
                                response.body()?.let { cache[type] = it }
                                _state.value = HomeState.Result(response.body())
                            } else {
                                log("Unsuccessful: " + response.message())
                                _state.value = HomeState.Error(response.message())
                            }
                        } else {
                            log("not equals ${type} and $placeType")
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
    }

    fun loadImage(photo: PlacePhoto): String {
        return "${BuildConfig.BASE_API_URL}maps/api/place/photo?key=${RetrofitBuilder.key}&photoreference=${photo.reference}&maxwidth=600"
    }

}