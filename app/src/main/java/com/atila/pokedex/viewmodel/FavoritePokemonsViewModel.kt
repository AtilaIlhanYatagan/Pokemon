package com.atila.pokedex.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.atila.pokedex.model.PokemonDetail
import com.atila.pokedex.service.PokemonDatabase
import kotlinx.coroutines.launch


class FavoritePokemonsViewModel(application: Application) : BaseViewModel(application) {

    val pokemonLiveData = MutableLiveData<ArrayList<PokemonDetail>>()
    val favoritePokemonCount = MutableLiveData<Int>()
    private lateinit var pokemonListFromRoom: ArrayList<PokemonDetail>

    fun refreshData() {
        launch {
            val dao = PokemonDatabase(getApplication()).pokemonDao()
            pokemonListFromRoom = dao.getAllPokemons() as ArrayList<PokemonDetail>
            pokemonLiveData.value = pokemonListFromRoom
        }
    }

    fun refreshFavoritePokemonCount(): Int {
        var favoritePokemonCountInt = 0
        launch {
            val dao = PokemonDatabase(getApplication()).pokemonDao()
            favoritePokemonCount.value = dao.getFavoriteCount()
            favoritePokemonCountInt = dao.getFavoriteCount()
        }
        return favoritePokemonCountInt
    }


}