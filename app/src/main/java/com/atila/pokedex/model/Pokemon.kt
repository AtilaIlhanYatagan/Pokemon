package com.atila.pokedex.model

import com.google.gson.annotations.SerializedName

data class Pokemon(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("weight")
    var weight : String = "100",
    @SerializedName("height")
    var height : String = "100",
    var hp : Int = 100,
    var atk : Int = 100,
    var def : Int = 100,
    var speed : Int = 100,
    var exp : Int = 100,

)    {

}