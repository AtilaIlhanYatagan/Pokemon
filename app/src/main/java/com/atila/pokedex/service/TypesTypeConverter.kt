package com.atila.pokedex.service

import androidx.room.TypeConverter
import com.atila.pokedex.model.Type
import com.google.gson.Gson

class TypesTypeConverter {
    @TypeConverter
    fun listToJson1(value: List<Type>?) = Gson().toJson(value)

    @TypeConverter
    fun JsonToList1(value: String) = Gson().fromJson(value,Array<Type>::class.java).toList()
}