package com.atila.pokedex.viewmodel

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atila.pokedex.model.PokemonDetail
import com.atila.pokedex.service.PokemonDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritePokemonsViewModel(application: Application) : BaseViewModel(application) {

    val pokemonLiveData = MutableLiveData<ArrayList<PokemonDetail>>()
    val favoritePokemonCount = MutableLiveData<Int>()
    private val dao = PokemonDatabase(getApplication()).pokemonDao()

    fun refreshData() {

        viewModelScope.launch(Dispatchers.Main) {
            pokemonLiveData.value = dao.getAllPokemons() as ArrayList<PokemonDetail>
        }
    }

    fun refreshFavoritePokemonCount() {

        viewModelScope.launch(Dispatchers.IO) {
            favoritePokemonCount.postValue(dao.getFavoriteCount())
        }
    }
}