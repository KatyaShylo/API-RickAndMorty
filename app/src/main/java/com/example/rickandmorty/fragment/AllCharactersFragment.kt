package com.example.rickandmorty.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.rickandmorty.R
import com.example.rickandmorty.adapter.CharacterAdapter
import com.example.rickandmorty.adapter.PagingData
import com.example.rickandmorty.api.ResultApi
import com.example.rickandmorty.api.ServiceRickAndMortyApi
import com.example.rickandmorty.databinding.FragmentAllCharactersBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.rickandmorty.api.Character

class AllCharactersFragment : Fragment() {

    private var _binding: FragmentAllCharactersBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val adapter by lazy {
        CharacterAdapter(requireContext()) { character ->
            findNavController().navigate(
                AllCharactersFragmentDirections.toDetailFragment(
                    character.id
                )
            )
        }
    }

    private val currentCharacters = mutableListOf<Character>()
    private var currentRequest: Unit? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAllCharactersBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            recyclerView.adapter = adapter
            with(toolbar) {
                menu
                    .findItem(R.id.action_search)
                    .actionView
                    .let { it as SearchView }
                    .setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(query: String): Boolean {
                            adapter.submitList(currentCharacters.filter { it.name.contains(query) }
                                .map { PagingData.ContentCharacter(it) })
                            return true
                        }
                    })
            }

            swipe.setOnRefreshListener {
                adapter.submitList(emptyList())
                executeRequest {
                    swipe.isRefreshing = false
                }
            }

            recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
                override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
                ) {
                    if (parent.getChildAdapterPosition(view) != parent.adapter?.itemCount) {
                        outRect.top = HORIZONTAL_SPACE_HEIGHT
                    }
                }
            })
            executeRequest()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun handlerException(e: Throwable) {
        Toast.makeText(requireContext(), e.message ?: "Error", Toast.LENGTH_SHORT)
            .show()
    }

    private fun executeRequest(onRequestFinished: () -> Unit = {}) {

        val executeRequestCallback = {
            onRequestFinished()
            currentRequest = null
        }

        if (currentRequest != null) return
        currentRequest = ServiceRickAndMortyApi.apiRickAndMorty
            .getCharacters(2)
            .enqueue(object : Callback<ResultApi> {
                override fun onResponse(
                    call: Call<ResultApi>,
                    response: Response<ResultApi>
                ) {
                    if (response.isSuccessful) {
                        val characters = response.body()?.characterList ?: return
                        currentCharacters.clear()
                        currentCharacters.addAll(characters)
                        val newCharacters = characters.map {
                            PagingData.ContentCharacter(it)
                        } + PagingData.Loading
                        adapter.submitList(newCharacters)
                    } else {
                        handlerException(Throwable())
                    }
                    executeRequestCallback()
                }

                override fun onFailure(call: Call<ResultApi>, t: Throwable) {
                    if (!call.isCanceled) {
                        handlerException(t)
                    }
                    executeRequestCallback()
                }
            })
    }

    companion object {

        private const val HORIZONTAL_SPACE_HEIGHT = 40
    }
}