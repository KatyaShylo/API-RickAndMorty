package com.example.rickandmorty.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyApi {
    @GET("character")
    fun getCharacters(@Query("page") page: Int): Call<ResultApi>


    @GET("character/{id}")
    fun getCharacterDetails(@Path("id") id: Int): Call<Character>
}