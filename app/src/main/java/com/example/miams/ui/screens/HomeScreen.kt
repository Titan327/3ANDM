package com.example.miams.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import androidx.compose.foundation.clickable
import androidx.compose.material3.Card

import com.example.miams.http.repository.RecipeRepository
import com.example.miams.http.types.SearchResponse
import com.example.miams.http.types.SearchResult
import kotlinx.coroutines.launch
@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    var searchText = remember { mutableStateOf("") }
    var recipes = remember { mutableStateOf(listOf<SearchResult>()) }
    var isLoading = remember { mutableStateOf(false) }

    LaunchedEffect(searchText.value) {
        isLoading.value = true
        scope.launch {
            try {
                val searchResponse = RecipeRepository().getSearchResult(1, searchText.value)
                recipes.value = searchResponse.results
            } catch (e: Exception) {
                Log.e("HomeScreen", "Error while getting search result for recipe", e)
            } finally {
                isLoading.value = false
            }
        }
    }

    Column {
        SearchBar(searchText.value, onSearchTextChange = { newText -> searchText.value = newText })

        if (isLoading.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            LazyColumn {
                items(recipes.value.size) { index ->
                    RecipeCard(recipes.value[index]) {
                        // Handle recipe click
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun RecipeCard(recipe: SearchResult, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberImagePainter(recipe.featured_image),
                contentDescription = "Recipe Image",
                modifier = Modifier.size(64.dp)
            )
            Text(recipe.title, Modifier.padding(start = 8.dp))
        }
    }
}