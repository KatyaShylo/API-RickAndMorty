package com.example.rickandmorty.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.rickandmorty.api.Character
import com.example.rickandmorty.R
import com.example.rickandmorty.api.ServiceRickAndMortyApi
import com.example.rickandmorty.databinding.FragmentDetailsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = requireNotNull(_binding)

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentDetailsBinding.inflate(inflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ServiceRickAndMortyApi.apiRickAndMorty
            .getCharacterDetails(args.id)
            .enqueue(object : Callback<Character> {
                override fun onResponse(call: Call<Character>, response: Response<Character>) {
                    if (response.isSuccessful) {
                        val characterDetail = response.body() ?: return
                        with(binding) {
                            imageViewAvatar.load(characterDetail.image)
                            textViewNameCharacter.text = characterDetail.name
                            textViewIdCharacter.text = "Id: ${characterDetail.id}"
                            textViewStatus.text = "Status: ${characterDetail.status}"
                            textViewSpecies.text = "Species: ${characterDetail.species}"
                            textViewGender.text = "Gender: ${characterDetail.gender}"
                        }
                    } else {
                        handlerException(Throwable())
                    }
                }

                override fun onFailure(call: Call<Character>, t: Throwable) {
                    handlerException(t)
                }
            })

        with(binding) {
            toolbarFragmentDetail.setNavigationOnClickListener {
                findNavController().navigate(R.id.all_characters_fragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun handlerException(e: Throwable) {
        Toast.makeText(requireContext(), e.message ?: "Error", Toast.LENGTH_SHORT)
            .show()
    }
}