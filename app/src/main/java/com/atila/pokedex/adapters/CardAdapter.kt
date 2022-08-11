package com.atila.pokedex.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.atila.pokedex.R
import com.atila.pokedex.databinding.RecyclerviewItemBinding
import com.atila.pokedex.model.Result
import com.atila.pokedex.util.createPlaceHolder
import com.atila.pokedex.util.downloadImage
import com.atila.pokedex.view.PokemonListFragmentDirections
import java.util.*
import kotlin.collections.ArrayList

class CardAdapter(private val pokemonList: ArrayList<Result>) :
    RecyclerView.Adapter<CardAdapter.CardViewHolder>(), Filterable {

    // arraylist to store filtered results
    var filteredList = ArrayList<Result>()

    init {
        filteredList = pokemonList
    }

    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerviewItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        with(holder) {

            // setting all recyclerview items attributes
            binding.pokemonListFragmentNameText.text = filteredList[position].name
            binding.listImage.downloadImage(
                filteredList[position].getImageURL(),
                createPlaceHolder(binding.listImage.context)
            )

            holder.itemView.setOnClickListener {
                val action =
                    PokemonListFragmentDirections.actionPokemonListFragmentToPokemonDetailFragment(
                        filteredList[position].name
                    )
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun updatePokemonList(newPokemonList: ArrayList<Result>) {
        pokemonList.clear()
        pokemonList.addAll(newPokemonList)
        notifyDataSetChanged()
    }

    //https://www.tugbaustundag.com/android-recyclerviewa-search-filter-ekleme/
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                //string that is searched
                val charSearch = constraint.toString()
                //if searchView is empty set the filteredList as the mainList
                if (charSearch.isEmpty()) {
                    filteredList = pokemonList
                } else {
                    val resultList = ArrayList<Result>()
                    for (row in pokemonList) {
                        //if the search string matches with the name add it to the list
                        if (row.name.lowercase(Locale.ROOT).contains(charSearch.lowercase(Locale.ROOT))) {
                            resultList.add(row)
                        }
                    }
                    filteredList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as ArrayList<Result>
                notifyDataSetChanged()
            }
        }
    }
}