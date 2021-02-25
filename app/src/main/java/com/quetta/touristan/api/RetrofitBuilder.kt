package com.quetta.touristan.api

import com.quetta.touristan.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_API_URL)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}