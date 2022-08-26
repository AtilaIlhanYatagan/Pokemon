package com.atila.pokedex.service

import androidx.room.TypeConverter
import com.atila.pokedex.model.Stat
import com.google.gson.Gson

class StatsTypeConverter {
    @TypeConverter
    fun listToJson(value: List<Stat>?) = Gson().toJson(value)

    @TypeConverter
    fun JsonToList(value: String) = Gson().fromJson(value,Array<Stat>::class.java).toList()
}
