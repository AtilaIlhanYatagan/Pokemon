package com.atila.pokedex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.atila.pokedex.model.Model
import com.atila.pokedex.model.Result
import com.atila.pokedex.service.PokemonAPIService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PokemonListViewModel : ViewModel() {

    val pokemons = MutableLiveData<ArrayList<Result>>()
    val errorMessage = MutableLiveData<Boolean>()
    val loading = MutableLiveData<Boolean>()
    private val apiService = PokemonAPIService()
    
        fun refreshData() {
            errorMessage.value = false
            loading.value = true

            apiService.getData().enqueue(object : Callback<Model> {

                override fun onResponse(call: Call<Model>, response: Response<Model>) {

                    val pokeList = response.body()?.results

                    pokeList?.let {
                        var pokemonList: ArrayList<Result> = it as ArrayList<Result>
                        pokemons.value = pokemonList
                        errorMessage.value = false
                        loading.value = false

                    }
                }

                override fun onFailure(call: Call<Model>, t: Throwable) {
                    errorMessage.value = true
                }

            })
        }
    }


