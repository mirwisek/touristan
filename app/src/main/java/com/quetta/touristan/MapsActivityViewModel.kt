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

class MapsActivityViewModel : ViewModel() {

    val state = MutableStateFlow<HomeState>(HomeState.Idle)


    val selectedChip = MutableLiveData<String>()

}