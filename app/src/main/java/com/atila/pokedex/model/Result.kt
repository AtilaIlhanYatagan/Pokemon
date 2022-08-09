package com.atila.pokedex.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Result (
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
): Serializable {

    fun getImageURL():String{
        val index = url.split("/".toRegex()).dropLast(1).last()
        return "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$index.png"
    }
}