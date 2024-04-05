package com.example.miams.LocalDB.Tables

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface RecipesDAO {

    @Upsert
    fun upsertRecipes(recipe: Recipes)

    @Query(value = "SELECT * FROM Recipes")
    fun getAllRecipes(): List<Recipes>

}