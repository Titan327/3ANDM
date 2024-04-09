package com.example.miams.LocalDB.Tables

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recipes")
data class Recipes(
    @PrimaryKey(autoGenerate = false) val id: Int = 0,
    val title: String = "",
    val featured_image: String = "",
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB) val image: ByteArray? = null,
    @ColumnInfo(defaultValue = "[]") val ingredients: String = "",
)
