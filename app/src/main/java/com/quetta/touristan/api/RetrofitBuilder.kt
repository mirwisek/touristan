package com.quetta.touristan.api

import com.quetta.touristan.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    // TODO: Change API key placing
    const val key = "AIzaSyAnFkgq-4Ki3Vj0ik7ld507lUar8n-61LQ"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    /**
     * Add the API key to each request
     */
    private val okHttpBuilder = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val originalReq = chain.request()
            val newUrl = originalReq.url.newBuilder().apply {
                addPathSegments("maps/api/place/textsearch/json")
//                addQueryParameter("key", key)
            }.build()
            val newReq = originalReq.newBuilder().url(newUrl).build()
            chain.proceed(newReq)
        }
        .addInterceptor(logging)

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpBuilder.build())
        .build()

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
}