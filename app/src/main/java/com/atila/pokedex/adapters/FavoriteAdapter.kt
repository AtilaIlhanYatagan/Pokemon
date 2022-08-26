package com.atila.pokedex.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.atila.pokedex.R
import com.atila.pokedex.databinding.RecyclerviewItemBinding
import com.atila.pokedex.model.PokemonDetail
import com.atila.pokedex.util.createPlaceHolder
import com.atila.pokedex.util.downloadImage
import com.atila.pokedex.util.setSafeOnClickListener


class FavoriteAdapter(
    private val pokemonList: ArrayList<PokemonDetail>,
    private val onItemClicked: (PokemonDetail, ImageView) -> Unit
) :
    RecyclerView.Adapter<FavoriteAdapter.CardViewHolder>() {

    inner class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = RecyclerviewItemBinding.bind(itemView)

        // function for setting the each list item's attributes
        fun bind(item: PokemonDetail) {
            // for setting attributes
            binding.pokemonListFragmentNameText.text = item.name
            binding.listImage.downloadImage(
                item.getImageURL(),
                createPlaceHolder(binding.listImage.context)
            )

            // for shared element transition
            binding.listImage.transitionName = item.name //-> unique transition name
            // click listener function is in the util/safeclicklistener
            binding.container.setSafeOnClickListener { onItemClicked(item, binding.listImage) }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recyclerview_item, parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {

        with(holder) {

            val item = pokemonList[position]
            bind(item)

            this.itemView.animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.recycler_view_load_anim
            )

            binding.listImage.setBackgroundColor(setBackgroundColor(pokemonList[position].types[0].type.name))

        }
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    fun updatePokemonList(newPokemonList: ArrayList<com.atila.pokedex.model.PokemonDetail>) {
        pokemonList.clear()
        pokemonList.addAll(newPokemonList)
        notifyDataSetChanged()
    }

    private fun setBackgroundColor(typeString: String): Int {
        when (typeString) {
            "normal" -> return Color.parseColor("#ffffff")
            "unknown" -> return Color.parseColor("#ffffff")
            "shadow" -> return Color.parseColor("#ffffff")
            "fighting" -> return Color.parseColor("#90B1C5")
            "flying" -> return Color.parseColor("#90B1C5")
            "poison" -> return Color.parseColor("#9F422A")
            "ground" -> return Color.parseColor("#AD7235")
            "rock" -> return Color.parseColor("#4B190E")
            "bug" -> return Color.parseColor("#179A55")
            "ghost" -> return Color.parseColor("#363069")
            "steel" -> return Color.parseColor("#5C756D")
            "fire" -> return Color.parseColor("#B22328")
            "water" -> return Color.parseColor("#0091EA")
            "grass" -> return Color.parseColor("#81C784")
            "electric" -> return Color.parseColor("#FFD600")
            "psychic" -> return Color.parseColor("#AC296B")
            "ice" -> return Color.parseColor("#7ECFF2")
            "dragon" -> return Color.parseColor("#378A94")
            "fairy" -> return Color.parseColor("#9E1A44")
            "dark" -> return Color.parseColor("#040706")

        }
        return 0
    }


}