package com.atila.pokedex.view

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.atila.pokedex.databinding.FragmentPokemonDetailBinding
import com.atila.pokedex.model.PokemonDetail
import com.atila.pokedex.util.createPlaceHolder
import com.atila.pokedex.util.downloadImage
import com.atila.pokedex.viewmodel.FavoritePokemonsViewModel
import com.atila.pokedex.viewmodel.PokemonDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PokemonDetailFragment : Fragment() {
    //viewModel declaration
    private lateinit var viewModel: PokemonDetailsViewModel
    private lateinit var favoriteViewModel: FavoritePokemonsViewModel

    //ViewBinding declarations
    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

    //PokemonListFragment Arg
    private var pokemonNameFromListFragment = ""
    private val attributeArrayList: ArrayList<Int> = arrayListOf()

    //to decide if the current pokemon is favorite
    private var isPokemonFavorite = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[PokemonDetailsViewModel::class.java]
        favoriteViewModel = ViewModelProvider(this)[FavoritePokemonsViewModel::class.java]

        //getting the arguments
        arguments?.let {
            pokemonNameFromListFragment = PokemonDetailFragmentArgs.fromBundle(it).pokemonName

            //viewModel.showPokemonData(pokemonNameFromListFragment) -> to get the values in a livedata
            viewModel.getPokemonDataFromApi(pokemonNameFromListFragment)

            // setting the isPokemonFavorite attribute to show appropriate button
            GlobalScope.launch(Dispatchers.Main) {
                isPokemonFavorite = viewModel.checkIfPokemonIsFavorite(pokemonNameFromListFragment)
            }

        }


        binding.addFavoriteButton.setOnClickListener {
            //store pokemon to room database
            viewModel.storePokemonToRoom(getPokemon())

            //update the ui
            binding.removeFavoriteButton.visibility = View.VISIBLE
            binding.addFavoriteButton.visibility = View.GONE

            //inform the user about adding the favorite
            Toast.makeText(
                activity, "${getPokemon().name} added to favorites",
                Toast.LENGTH_SHORT
            ).show()

            // calling activity method from fragment to set the badge count
            // refreshPokemonCount is a suspend function (async-await) (main thread used bc of that)
            GlobalScope.launch(Dispatchers.Main) {
                val favoriteCount = viewModel.refreshFavoritePokemonCount()
                (activity as MainActivity?)?.refreshBadgeCount(favoriteCount)
            }
        }


        binding.removeFavoriteButton.setOnClickListener {
            //remove the pokemon from database
            viewModel.removePokemonFromRoom(getPokemon())

            //update the ui
            binding.removeFavoriteButton.visibility = View.GONE
            binding.addFavoriteButton.visibility = View.VISIBLE

            //inform the user about adding the favorite
            Toast.makeText(
                activity,
                "${getPokemon().name} removed from favorites",
                Toast.LENGTH_SHORT
            ).show()

            // calling activity method from fragment to set the badge count
            // refreshPokemonCount is a suspend function (async-await) (main thread used bc of that)
            GlobalScope.launch(Dispatchers.Main) {
                val favoriteCount = viewModel.refreshFavoritePokemonCount()
                (activity as MainActivity?)?.refreshBadgeCount(favoriteCount)
            }
        }

        observeLiveData()
    }


    private fun observeLiveData() {

        viewModel.pokemonLiveData.observe(viewLifecycleOwner, Observer { pokemon ->

            pokemon?.let {
                //Set the button visibility on the launch  
                if (isPokemonFavorite == 0) {
                    binding.removeFavoriteButton.visibility = View.GONE
                    binding.addFavoriteButton.visibility = View.VISIBLE
                } else if (isPokemonFavorite == 1) {
                    binding.removeFavoriteButton.visibility = View.VISIBLE
                    binding.addFavoriteButton.visibility = View.GONE
                }

                // setting up the image and attributes
                binding.pokemonDetailNameText.text = it.name
                binding.pokemonDetailHeightText.text = it.height.toString() + " M"
                binding.pokemonDetailWeightText.text = (it.weight / 10).toString() + " KG"
                binding.pokemonDetailFragmentId.text = "#" + it.id
                binding.detailImage.downloadImage(
                    it.getImageURL(),
                    createPlaceHolder(binding.detailImage.context)
                )

                binding.detailImage.setBackgroundColor((viewModel.setBackgroundColor(it.types[0].type.name)))
                binding.myToolbar.setBackgroundColor(viewModel.setBackgroundColor(it.types[0].type.name))

                for (i in it.stats.indices) {
                    attributeArrayList.add(it.stats[i].base_stat)
                    //println(attributeArrayList[i])
                }

                ObjectAnimator.ofInt((binding.progressBarHP), "progress", attributeArrayList[0]).apply {
                    duration = 100
                    start()
                }


                ObjectAnimator.ofInt((binding.progressBarATK), "progress", attributeArrayList[1])
                    .setDuration(2000).start()
                ObjectAnimator.ofInt((binding.progressBarDEF), "progress", attributeArrayList[2])
                    .setDuration(2000).start()
                ObjectAnimator.ofInt((binding.progressBarEXP), "progress", attributeArrayList[3])
                    .setDuration(2000).start()
                ObjectAnimator.ofInt((binding.progressBarSPD), "progress", attributeArrayList[4])
                    .setDuration(2000).start()

            }
        })
    }

    private fun animateProgressBar() {



    }

    // to get the pokemon detail object that clicked ( for room method calls)
    private fun getPokemon(): PokemonDetail {
        lateinit var pokemon1: PokemonDetail
        viewModel.pokemonLiveData.observe(viewLifecycleOwner, Observer { pokemon ->
            pokemon1 = pokemon
        })
        return pokemon1
    }
}