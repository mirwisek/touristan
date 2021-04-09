package com.quetta.touristan

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.quetta.touristan.model.HomeState
import kotlinx.coroutines.flow.MutableStateFlow

class MapsActivityViewModel : ViewModel() {

    val state = MutableStateFlow<HomeState>(HomeState.Idle)


    val selectedCategory = MutableLiveData<String>()

}