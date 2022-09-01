package com.example.rickandmorty.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.rickandmorty.api.Character
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.databinding.ItemCharacterBinding
import com.example.rickandmorty.databinding.ItemLoadingBinding

class CharacterAdapter(
    context: Context,
    private val onCharacterClicked: (Character) -> Unit
) : ListAdapter<PagingData<Character>, RecyclerView.ViewHolder>(DIFF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagingData.ContentCharacter -> TYPE_CHARACTER
            PagingData.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CHARACTER -> {
                CharacterViewHolder(
                    binding = ItemCharacterBinding.inflate(layoutInflater, parent, false),
                    onCharacterClicked = onCharacterClicked
                )
            }
            TYPE_LOADING -> {
                LoadingViewHolder(
                    binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> error("Unsupported ViewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val character = getItem(position)) {
            is PagingData.ContentCharacter -> {
                checkNotNull(holder as CharacterViewHolder) { "Incorrect ViewHolder $character" }
                holder.bind(character.data)
            }
            PagingData.Loading -> {}
        }
    }

    companion object {

        private const val TYPE_CHARACTER = 0
        private const val TYPE_LOADING = -1

        private val DIFF_UTIL = object :
            DiffUtil.ItemCallback<PagingData<Character>>() {
            override fun areItemsTheSame(
                oldItem: PagingData<Character>,
                newItem: PagingData<Character>
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: PagingData<Character>,
                newItem: PagingData<Character>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}