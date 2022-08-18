package com.atila.pokedex.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.atila.pokedex.R
import com.atila.pokedex.databinding.RecyclerviewItemBinding
import com.atila.pokedex.model.Result
import com.atila.pokedex.util.createPlaceHolder
import com.atila.pokedex.util.downloadImage
import com.atila.pokedex.view.PokemonListFragmentDirections
import java.util.*
import kotlin.collections.ArrayList

class CardAdapter(
    private val pokemonList: ArrayList<Result>,
    private val onItemClicked: (Result, ImageView) -> Unit
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>(), Filterable {

    // arraylist to store filtered results
    var filteredList = ArrayList<Result>()

    init {
        filteredList = pokemonList
    }

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = RecyclerviewItemBinding.bind(itemView)

        // function for setting the each list items attributes
        fun bind(item: Result) {
            // for setting attributes
            binding.pokemonListFragmentNameText.text = item.name
            binding.listImage.downloadImage(
                item.getImageURL(),
                createPlaceHolder(binding.listImage.context)
            )
            // for shared element transition
            binding.listImage.transitionName = item.name //-> unique transition name
            binding.container.setOnClickListener { onItemClicked(item, binding.listImage) }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return CardViewHolder(view)
    }


    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        with(holder) {
            val item = filteredList[position]
            bind(item)
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
                        if (row.name.lowercase(Locale.ROOT)
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
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

