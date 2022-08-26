package com.atila.pokedex.service

import com.atila.pokedex.model.Model
import com.atila.pokedex.model.PokemonDetail
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PokemonAPIService {
    private val BASE_URL = "https://pokeapi.co/api/v2/"
    private val api = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build().create(PokeAPI::class.java)

    fun getData(): Call<Model> {
        return api.getPokemon(30, 0)
    }

    suspend fun getPokemonData(name: String): Response<PokemonDetail> {
        return api.getPokemonInfo(name)
    }

}