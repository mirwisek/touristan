package com.quetta.touristan.api

import com.quetta.touristan.model.Places
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/photo")
    fun getPhoto(
        @Query("photoreference") reference: String,
        @Query("maxwidth") maxWidth: Int? = null,
        @Query("key") key: String = RetrofitBuilder.key
    ): Call<Places>

    @GET(".")
    fun getPlaces(
        @Query("type") type: String = "tourist_attraction",
        @Query("key") key: String = RetrofitBuilder.key
    ): Call<Places>

}