package com.example.miams.http.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("count") val count:Int,
    @SerialName("next") val next:String? =null,
    @SerialName("previous") val previous:String? =null,
    @SerialName("results") val results:List<SearchResult>
)

@Serializable
data class SearchResult(
    @SerialName("pk") val pk:Int,
    @SerialName("title") val title:String,
    @SerialName("publisher") val publiser:String,
    @SerialName("featured_image") val featured_image:String,
    @SerialName("rating") val rating:Int,
    @SerialName("source_url") val source_url:String,
    @SerialName("description") val description:String,
    @SerialName("cooking_instructions") val cooking_instructions:String? =null,
    @SerialName("ingredients") val ingredients:List<String>,
    @SerialName("date_added") val date_added:String,
    @SerialName("date_updated") val date_updated:String,
    @SerialName("long_date_added") val long_date_added:Int,
    @SerialName("long_date_updated") val long_date_updated:Int
)