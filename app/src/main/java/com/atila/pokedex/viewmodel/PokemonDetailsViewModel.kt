package com.atila.pokedex.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atila.pokedex.model.*
import com.atila.pokedex.service.PokemonAPIService
import com.atila.pokedex.service.PokemonDatabase
import kotlinx.coroutines.*


class PokemonDetailsViewModel(application: Application) : BaseViewModel(application) {

    val pokemonLiveData: MutableLiveData<PokemonDetail> = MutableLiveData<PokemonDetail>()
    private val apiService = PokemonAPIService()

    //https://medium.com/androiddevelopers/easy-coroutines-in-android-viewmodelscope-25bffb605471
    fun setLiveData(name: String) {
        viewModelScope.launch {
            pokemonLiveData.postValue(getPokemonDataFromApi(name)!!)
        }
    }

    private suspend fun getPokemonDataFromApi(name: String): PokemonDetail? =
        withContext(Dispatchers.IO) {
            val response = apiService.getPokemonData(name)
            var pokemonFromApi: PokemonDetail? = null
            if (response.isSuccessful) {
                pokemonFromApi = response.body()
            }
            return@withContext pokemonFromApi
        }


    suspend fun checkIfPokemonIsFavorite(name: String): Int {
        var isfavorite = 0

        val value = viewModelScope.async {

            val dao = PokemonDatabase(getApplication()).pokemonDao()
            isfavorite = dao.isFavorite(name)

        }
        value.await()
        return isfavorite
    }

    fun storePokemonToRoom(pokemon: PokemonDetail) {
        launch {
            val dao = PokemonDatabase(getApplication()).pokemonDao()
            dao.insertPokemonToDatabase(pokemon)
        }
    }

    fun removePokemonFromRoom(name: String) {
        launch {
            val dao = PokemonDatabase(getApplication()).pokemonDao()
            dao.deletePokemonFromFavorites(name)
        }
    }

    fun setBackgroundColor(typeString: String): Int {
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