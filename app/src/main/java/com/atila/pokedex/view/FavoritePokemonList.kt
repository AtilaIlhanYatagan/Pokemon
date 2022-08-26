package com.atila.pokedex.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.atila.pokedex.adapters.FavoriteAdapter
import com.atila.pokedex.databinding.FragmentFavoritePokemonListBinding
import com.atila.pokedex.databinding.FragmentPokemonListBinding
import com.atila.pokedex.viewmodel.FavoritePokemonsViewModel
import kotlin.isInitialized as isInitialized


class FavoritePokemonList : Fragment() {

    private lateinit var viewModel: FavoritePokemonsViewModel
    private lateinit var adapter: FavoriteAdapter

    private var _binding: FragmentFavoritePokemonListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritePokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //fragment viewModel connection
        viewModel = ViewModelProvider(this)[FavoritePokemonsViewModel::class.java]
        viewModel.refreshData()

        initRecyclerView()

        binding.favoriteRecyclerView.layoutManager = GridLayoutManager(context, 2)

        observeLiveData()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        //two card in each row, adapter fragment connection
        adapter = FavoriteAdapter(arrayListOf(), onItemClicked = { result, imageView ->
            val extras = FragmentNavigatorExtras(
                imageView to result.name
            )
            val action =
                FavoritePokemonListDirections.actionFavoritePokemonListToPokemonDetailFragment(
                    result.name
                )

            findNavController().navigate(action, extras)

        })
        binding.favoriteRecyclerView.adapter = adapter
    }


    private fun observeLiveData() {

        viewModel.pokemonLiveData.observe(viewLifecycleOwner, Observer { pokemons ->
            pokemons?.let {

                pokemons.let {

                    if (it.isEmpty()) {
                        binding.favoritePokemonListText.visibility = View.VISIBLE
                        binding.favoriteRecyclerView.visibility = View.GONE
                    } else if (it.isNotEmpty()) {
                        binding.favoritePokemonListText.visibility = View.GONE
                        binding.favoriteRecyclerView.visibility = View.VISIBLE
                        adapter.updatePokemonList(pokemons)
                    }
                }
            }
        })
    }
}
