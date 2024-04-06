package com.example.miams.LocalDB.Tables

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert


@Dao
interface RecipesDAO {

    @Upsert
    suspend fun upsertRecipes(recipe: Recipes)

    @Query(value = "SELECT * FROM Recipes")
    suspend fun getAllRecipes(): List<Recipes>

}