package com.atila.pokedex.view

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.atila.pokedex.databinding.FragmentPokemonDetailBinding
import com.atila.pokedex.model.PokemonDetail
import com.atila.pokedex.model.Stat
import com.atila.pokedex.util.setSafeOnClickListener
import com.atila.pokedex.viewmodel.PokemonDetailsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PokemonDetailFragment : Fragment() {
    //viewModel declaration
    private lateinit var viewModel: PokemonDetailsViewModel

    //ViewBinding declarations
    private var _binding: FragmentPokemonDetailBinding? = null
    private val binding get() = _binding!!

    //PokemonListFragment Arg
    private var pokemonNameFromListFragment = ""

    //to decide if the current pokemon is favorite
    private var isPokemonFavorite = 0

    private var progressBarList: ArrayList<ProgressBar> = arrayListOf()

    //used for adding the favorite pokemon to the database
    private lateinit var currentPokemon: PokemonDetail

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPokemonDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //shared element transition
        binding.detailImage.transitionName = pokemonNameFromListFragment

        print("onstart çağırıldı")

        // setting the isPokemonFavorite attribute to show appropriate button
        GlobalScope.launch(Dispatchers.IO) {
            isPokemonFavorite = viewModel.checkIfPokemonIsFavorite(pokemonNameFromListFragment)
        }


        addProgressBar()
        observeLiveData()

        //viewModel.getPokemonDataFromApi(pokemonNameFromListFragment) -> called by setLiveData
        viewModel.setLiveData(pokemonNameFromListFragment)


        // bu fora statlist verirsem belki olur
        for (i in 0..4) {
            val rands  = (0..150).random()
//          val currentProgress = currentPokemon.stats[0].base_stat
            animateProgressBar(progressBarList[i], rands)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //shared element transition
        postponeEnterTransition()

        viewModel = ViewModelProvider(this)[PokemonDetailsViewModel::class.java]

        //getting the arguments
        arguments?.let {
            pokemonNameFromListFragment = PokemonDetailFragmentArgs.fromBundle(it).pokemonName
        }


        binding.addFavoriteButton.setSafeOnClickListener {
            //store pokemon to room database
            viewModel.storePokemonToRoom(currentPokemon)

            //update the ui
            binding.removeFavoriteButton.visibility = View.VISIBLE
            binding.addFavoriteButton.visibility = View.GONE

            //inform the user about adding the favorite
            Toast.makeText(
                activity, "$pokemonNameFromListFragment added to favorites",
                Toast.LENGTH_SHORT
            ).show()

            // calling activity method from fragment to set the badge count
            (activity as MainActivity?)?.increaseBadgeCount()

        }

        binding.removeFavoriteButton.setSafeOnClickListener {
            //remove the pokemon from database
            viewModel.removePokemonFromRoom(pokemonNameFromListFragment)

            //update the ui
            binding.removeFavoriteButton.visibility = View.GONE
            binding.addFavoriteButton.visibility = View.VISIBLE

            //inform the user about adding the favorite
            Toast.makeText(
                activity,
                "$pokemonNameFromListFragment removed from favorites",
                Toast.LENGTH_SHORT
            ).show()

            // calling activity method from fragment to set the badge count
            (activity as MainActivity?)?.decreaseBadgeCount()

        }
    }

    private fun observeLiveData() {
        viewModel.pokemonLiveData.observe(viewLifecycleOwner) {

            //Set the button visibility on the launch
            setFavoriteButton()

            currentPokemon = it

            // setting up the image and attributes
            binding.pokemonDetailNameText.text = it.name
            binding.pokemonDetailHeightText.text = it.height.toString() + " M"
            binding.pokemonDetailWeightText.text = (it.weight / 10).toString() + " KG"
            binding.pokemonDetailFragmentId.text = "# ${it.id}"
            binding.detailImage.setBackgroundColor((viewModel.setBackgroundColor(it.types[0].type.name)))
            binding.myToolbar.setBackgroundColor(viewModel.setBackgroundColor(it.types[0].type.name))

            Glide.with(this)
                .load(it.getImageURL())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .into(binding.detailImage)

            /*for (i in 0..4) {
                val currentProgress = currentPokemon.stats[i].base_stat
                animateProgressBar(progressBarList[i], currentProgress)
            }*/
        }
    }

    private fun animateProgressBar(progressBar: ProgressBar, value: Int) {

        println("animate progress bar geldi $progressBar $value")
        progressBar.progress = value
        //ObjectAnimator.ofInt((progressBar), "progress", value).setDuration(400).start() //-> value 50 versen bile çalışmıyor

    }

    private fun addProgressBar() {
        progressBarList.clear()

        progressBarList.add(binding.progressBarHP)
        progressBarList.add(binding.progressBarATK)
        progressBarList.add(binding.progressBarDEF)
        progressBarList.add(binding.progressBarEXP)
        progressBarList.add(binding.progressBarSPD)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setFavoriteButton() {
        if (isPokemonFavorite == 0) {
            binding.removeFavoriteButton.visibility = View.GONE
            binding.addFavoriteButton.visibility = View.VISIBLE
        } else if (isPokemonFavorite == 1) {
            binding.removeFavoriteButton.visibility = View.VISIBLE
            binding.addFavoriteButton.visibility = View.GONE
        }
    }

}