package com.atila.pokedex.viewmodel

import android.app.Application
import android.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.atila.pokedex.model.*
import com.atila.pokedex.service.PokemonAPIService
import com.atila.pokedex.service.PokemonDatabase
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonDetailsViewModel(application: Application) : BaseViewModel(application) {

    val pokemonLiveData: MutableLiveData<PokemonDetail> = MutableLiveData<PokemonDetail>()
    private val apiService = PokemonAPIService()

    fun storePokemonToRoom(pokemon: PokemonDetail) {
        launch {
            //pokemonLiveData.value?.isFavorite = true
            val dao = PokemonDatabase(getApplication()).pokemonDao()
            dao.insertPokemonToDatabase(pokemon)
        }
    }

    fun removePokemonFromRoom(pokemon: PokemonDetail) {
        launch {
            // pokemonLiveData.value?.isFavorite = false
            val dao = PokemonDatabase(getApplication()).pokemonDao()
            dao.deletePokemonFromFavorites(pokemon.name)
        }
    }

    fun getPokemonDataFromApi(name: String) {

        apiService.getPokemonData(name).enqueue(object : Callback<PokemonDetail> {

            override fun onResponse(call: Call<PokemonDetail>, response: Response<PokemonDetail>) {

                val pokemonDetail = response.body()
                pokemonDetail?.let {
                    pokemonLiveData.value = pokemonDetail
                }
            }

            override fun onFailure(call: Call<PokemonDetail>, t: Throwable) {
                //TODO("Not yet implemented")
            }

        })

    }

    suspend fun refreshFavoritePokemonCount(): Int {
        var favoritePokemonCountInt = 0
        val value = GlobalScope.async {

            val dao = PokemonDatabase(getApplication()).pokemonDao()
            favoritePokemonCountInt = dao.getFavoriteCount()

        }
        value.await()
        return favoritePokemonCountInt
    }

    suspend fun checkIfPokemonIsFavorite(name: String): Int {
        var isfavorite = 0

            val value = GlobalScope.async {

                val dao = PokemonDatabase(getApplication()).pokemonDao()
                isfavorite = dao.isFavorite(name)

            }
            value.await()

        return isfavorite
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