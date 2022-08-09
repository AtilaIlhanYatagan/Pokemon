package com.atila.pokedex.service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.atila.pokedex.model.PokemonDetail

// converter is for converting stats object in PokemonDetail

@Database(entities = arrayOf(PokemonDetail::class), version = 3)
@TypeConverters(StatsTypeConverter::class,TypesTypeConverter::class)
abstract class PokemonDatabase : RoomDatabase() {

    //Database dao connection
    abstract fun pokemonDao(): PokemonDAO

    //Singleton to make one and only one thread can access the database
    companion object {

        //Anlatım : btk akademi ileri room database oluşturmak
        @Volatile
        private var instance: PokemonDatabase? = null
        private var lock = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(lock) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext, PokemonDatabase::class.java, "pokemonDatabase"
         ).fallbackToDestructiveMigration().build()//build()

    }


}