package com.atila.pokedex.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter.FilterListener
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.atila.pokedex.adapters.CardAdapter
import com.atila.pokedex.databinding.FragmentPokemonListBinding
import com.atila.pokedex.util.animateViewFromBottomToTop
import com.atila.pokedex.util.hideKeyboard
import com.atila.pokedex.viewmodel.PokemonListViewModel


class PokemonListFragment : Fragment() {

    //viewModel and adapter declarations
    private lateinit var viewModel: PokemonListViewModel
    private lateinit var adapter: CardAdapter

    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        observeLiveData()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //fragment viewModel connection
        viewModel = ViewModelProvider(this)[PokemonListViewModel::class.java]

        viewModel.refreshData()

        //RecyclerView - Adapter connection
        initRecyclerView()

        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)



        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {


                val listener = FilterListener {
                    if (adapter.itemCount == 0) {
                        binding.recyclerView.visibility = View.GONE
                        binding.noElementFount.visibility = View.VISIBLE
                        binding.imageNoPokemon.visibility = View.VISIBLE
                        hideKeyboard()
                        animateViewFromBottomToTop(binding.imageNoPokemon)
                        animateViewFromBottomToTop(binding.noElementFount)
                    } else {
                        binding.recyclerView.visibility = View.VISIBLE
                        binding.noElementFount.visibility = View.GONE
                        binding.imageNoPokemon.visibility = View.GONE
                    }
                }
                /* adapter.filter.filter(newText, listener)
                 return false

                 //optional ( filter the list after at least three letters )
                if (newText?.length!! > 2) {
                    adapter.filter.filter(newText, listener)
                    return false
                }*/
                return if (newText?.length!! == 0) {
                    adapter.filter.filter("", listener)
                    false
                } else {
                    adapter.filter.filter(newText, listener)
                    false
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView() {
        //two card in each row, adapter fragment connection
        adapter = CardAdapter(arrayListOf(), onItemClicked = { result, imageView ->
            val extras = FragmentNavigatorExtras(
                imageView to result.name
            )
            val action =
                PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailFragment(
                    result.name
                )

            findNavController().navigate(action, extras)

        })
        binding.recyclerView.adapter = adapter
    }

    private fun observeLiveData() {

        viewModel.pokemons.observe(viewLifecycleOwner, Observer { pokemons ->

            pokemons.let {
                adapter.updatePokemonList(pokemons)
            }

            binding.recyclerView.visibility = View.VISIBLE
            binding.noElementFount.visibility = View.GONE

        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { error ->
            error?.let {

                if (it) {
                    binding.hataMesaji.visibility = View.VISIBLE
                    binding.recyclerView.visibility = View.GONE
                } else {
                    binding.hataMesaji.visibility = View.GONE
                }
            }
        })

        viewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            loading?.let {

                if (it) {
                    binding.hataMesaji.visibility = View.GONE
                    binding.recyclerView.visibility = View.GONE
                    binding.progressBar.visibility = View.VISIBLE
                } else {
                    binding.progressBar.visibility = View.GONE
                }
            }

        })
    }
}