package com.example.miams.LocalDB.Tables

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow


@Dao
interface RecipesDAO {

    @Upsert
    suspend fun upsertRecipes(recipe: Recipes)

    /*
    @Query(value = "SELECT * FROM Recipes")
    fun getAllRecipes(): Flow<List<Recipes>>
     */

    @Query(value = "SELECT * FROM Recipes")
    suspend fun getAllRecipes(): List<Recipes>

    @Query(value = "DELETE FROM Recipes")
    suspend fun deleteAllRecipes()

    @Query("SELECT * FROM Recipes WHERE id = :id")
    suspend fun getRecipeById(id: Int): Recipes


}