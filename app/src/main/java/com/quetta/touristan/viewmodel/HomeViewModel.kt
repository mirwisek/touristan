package com.quetta.touristan.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.quetta.touristan.R
import com.quetta.touristan.api.Repository
import com.quetta.touristan.log
import com.quetta.touristan.model.*
import com.quetta.touristan.readRawJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.reflect.Type

class HomeViewModel(val app: Application, private val repository: Repository) : AndroidViewModel(app) {

    companion object {
        const val COUNTRY_CODE = "PK"
    }

    private val _state = MutableStateFlow<HomeState>(HomeState.Idle)
    val state: StateFlow<HomeState> get() = _state

    val allPlaces = Transformations.map(state.asLiveData()) { s ->
        when(s) {
            is HomeState.Result -> s.places
            else -> null
        } ?: listOf()
    }


    private fun loadPlaceFiles(type: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val jsonString = app.readRawJson(PlaceType.getJsonResource(type))
                val tType: Type = TypeToken.getParameterized(List::class.java, PlaceItem::class.java).type
                val places = Gson().fromJson<List<PlaceItem>>(jsonString, tType).shuffled()
                repository.storeInCache(type, places)
                _state.value = HomeState.Result(places)
            } catch (e: Exception) {
                log("Unsuccessful: " + e.localizedMessage)
                _state.value = HomeState.Error(e.localizedMessage)
            }
        }
    }


    fun getPlaces(type: String) {
        viewModelScope.launch {
            _state.value = HomeState.Loading
            val cache = repository.cache
            if(cache.containsKey(type)) {
                cache[type]?.let { item ->
                    _state.value = HomeState.Result(item)
                }
            } else {
                loadPlaceFiles(type)
            }
        }
    }

}