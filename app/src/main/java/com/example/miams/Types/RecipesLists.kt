package com.example.miams.Types


data class RecipesLists(
    val pk:Int,
    val title:String,
    val featured_image:String?=null,
    val image:ByteArray?=null,
    val ingredients:List<String>,
)
