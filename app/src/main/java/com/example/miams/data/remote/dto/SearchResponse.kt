package com.example.miams.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val pk: Int,
    val title: String,
    val publisher: String,
    val featured_image: String,
    val rating: Int,
    val source_url: String,
    val description: String,
    val cooking_instructions: String,
    val ingredients: Array<String>,
    val date_added: String,
    val date_updated: String,
    val long_date_added: Int,
    val long_date_updated: Int,
)
