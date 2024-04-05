package com.example.miams.LocalDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miams.LocalDB.Tables.RecipesDAO
import com.example.miams.LocalDB.Tables.Recipes

@Database(
    entities = [Recipes::class],
    version = 1
)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun RecipesDAO(): RecipesDAO

    companion object {
        private var instance: RecipesDatabase? = null

        @Synchronized
        fun getInstance(ctx: Context): RecipesDatabase {
            if(instance == null)
                instance = Room.databaseBuilder(
                    ctx.applicationContext,
                    RecipesDatabase::class.java,
                    "RecipesDatabase"
                ).fallbackToDestructiveMigration()
                    .build()

            return instance!!
        }
    }
}