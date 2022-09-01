package com.example.rickandmorty.api

import com.google.gson.annotations.SerializedName

data class ResultApi(
    @SerializedName("results")
    val characterList: List<Character>
)
