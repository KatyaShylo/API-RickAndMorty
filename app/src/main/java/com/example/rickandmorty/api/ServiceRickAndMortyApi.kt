package com.example.rickandmorty.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ServiceRickAndMortyApi {

    private const val BASE_URL = "https://rickandmortyapi.com/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(
            OkHttpClient.Builder()
                .build()
        )
        .build()

    val apiRickAndMorty = retrofit.create<RickAndMortyApi>()
}