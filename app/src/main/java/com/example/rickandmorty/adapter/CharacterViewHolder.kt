package com.example.rickandmorty.adapter

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.rickandmorty.api.Character
import com.example.rickandmorty.databinding.ItemCharacterBinding

class CharacterViewHolder(
    private val binding: ItemCharacterBinding,
    private val onCharacterClicked: (Character) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: Character) {
        with(binding) {
            imageAvatar.load(item.image)
            textName.text = item.name

            root.setOnClickListener {
                onCharacterClicked(item)
            }
        }
    }
}