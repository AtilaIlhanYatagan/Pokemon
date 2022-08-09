package com.atila.pokedex.service

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.atila.pokedex.model.PokemonDetail

@Dao
interface PokemonDAO {

    //to insert one pokemon
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPokemonToDatabase(pokemon : PokemonDetail) : Long

    //to insert all pokemons
    @Insert
    suspend fun insertAll(vararg pokemon : PokemonDetail) : List<Long>

    // to get all pokemons
    @Query("SELECT * FROM PokemonDetail")
    suspend fun getAllPokemons() : List<PokemonDetail>

    //to get single pokemon
    @Query("SELECT * FROM PokemonDetail WHERE name = :name")
    suspend fun getPokemonForDetail(name : String) : PokemonDetail

    // to delete one pokemon
    @Query("DELETE FROM PokemonDetail WHERE name = :name")
    suspend fun deletePokemonFromFavorites(name : String) : Int

    //to delete all pokemons
    @Query("DELETE FROM PokemonDetail" )
    suspend fun deleteAllFavoritePokemons() : Int

    @Query("SELECT COUNT(*) FROM PokemonDetail ")
    suspend fun getFavoriteCount() : Int

    @Query("SELECT COUNT(*) FROM PokemonDetail WHERE name = :name")
    suspend fun isFavorite(name: String) : Int




}