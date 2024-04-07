package com.example.miams.Types


data class RecipesLists(

    val id: Int, // Ensure this property exists
    val title:String,
    val featured_image:String?=null,
    val image:ByteArray?=null,
)
