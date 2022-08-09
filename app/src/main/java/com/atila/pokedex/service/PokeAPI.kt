package com.atila.pokedex.service

import com.atila.pokedex.model.Model
import com.atila.pokedex.model.PokemonDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeAPI {

    //https://pokeapi.co/api/v2/pokemon

    @GET("pokemon?")
    fun getPokemon(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<Model>

    @GET("pokemon/{name}")
    fun  getPokemonInfo(@Path("name") name: String): Call<PokemonDetail>

}