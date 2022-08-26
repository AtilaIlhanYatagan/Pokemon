package com.atila.pokedex.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class PokemonDetail(
    @PrimaryKey
    val id: Int,

    @ColumnInfo(name = "weight")
    val weight: Int,

    @ColumnInfo(name = "height")
    val height: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "stats")
    val stats: List<Stat>,

    @ColumnInfo(name = "types")
    val types: List<Type>

/*

    @Ignore
    val abilities: List<Ability> = arrayListOf(),
    @Ignore
    val base_experience: Int = 0,
    @Ignore
    val forms: List<Form> = arrayListOf(),
    @Ignore
    val game_indices: List<Gameİndice> = arrayListOf(),
    @Ignore
    val held_items: List<Any> = arrayListOf(),
    @Ignore
    val is_default: Boolean = false,
    @Ignore
    val location_area_encounters: String = "",
    @Ignore
    val moves: List<Move> = arrayListOf(),
    @Ignore
    val order: Int = 0,
    @Ignore
    val past_types: List<Any> = arrayListOf(),
    @Ignore
    val species: Species = Any() as Species,
    @Ignore
    val sprites: Sprites = Any() as Sprites,
    @Ignore
    ,*/

) : Serializable {

    fun getImageURL(): String {
        val index = this.id
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
    }
}