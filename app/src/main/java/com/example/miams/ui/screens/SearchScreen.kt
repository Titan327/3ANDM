package com.example.miams.ui.screens

import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.miams.http.repository.RecipeRepository
import com.example.miams.http.types.SearchResponse
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(){
    val scope = rememberCoroutineScope()
    val search = remember { mutableStateOf<SearchResponse?>(null) }

    LaunchedEffect(true) {
        scope.launch {
            try {
                search.value = RecipeRepository().getSearchResult(1, "beef")
                Log.d("test","on est la")

            } catch (e: Exception) {
                Log.e("SearchScreen", "Error while getting search result for recipe", e)
            }
        }
    }

    if (search.value != null) {
        Text(text = "Search: ${search.value!!.count}")
    } else {
        Text(text = "Loading search...")
    }
}