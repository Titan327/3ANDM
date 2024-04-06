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

import com.example.miams.http.repository.RecipeRepository
import com.example.miams.http.types.SearchResponse
import kotlinx.coroutines.launch
@Composable
fun HomeScreen() {
    var searchText = remember { mutableStateOf("") }

    Column {
        SearchBar(searchText.value, onSearchTextChange = { newText -> searchText.value = newText })

        // Replace this with your actual loading state
        val isLoading = false

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            // Replace this with your actual list of recipes
            val recipes = listOf<Recipe>()

            LazyColumn {
                items(recipes.size) { index ->
                    RecipeCard(recipes[index]) {
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
fun RecipeCard(recipe: Recipe, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberImagePainter(recipe.imageUrl),
                contentDescription = "Recipe Image",
                modifier = Modifier.size(64.dp)
            )
            Text(recipe.title, Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun Card(modifier: Any, content: @Composable () -> Unit) {

}

data class Recipe(val title: String, val imageUrl: String)