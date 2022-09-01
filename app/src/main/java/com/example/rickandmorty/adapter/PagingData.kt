package com.example.rickandmorty.adapter

sealed class PagingData<out T> {

    data class ContentCharacter<T>(val data: T) : PagingData<T>()

    object Loading : PagingData<Nothing>()
}