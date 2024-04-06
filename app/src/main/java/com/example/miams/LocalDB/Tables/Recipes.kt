package com.example.miams.LocalDB.Tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipes")
data class Recipes(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
)
