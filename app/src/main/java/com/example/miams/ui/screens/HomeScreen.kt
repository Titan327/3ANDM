package com.example.miams.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.miams.http.repository.RecipeRepository
import com.example.miams.http.types.SearchResponse
import com.example.miams.http.types.SearchResult
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    var searchText = remember { mutableStateOf("") }
    var recipes = remember { mutableStateOf(listOf<SearchResult>()) }
    var isLoading = remember { mutableStateOf(false) }
    val categories = listOf("Beef", "Chicken", "Dessert")
    val scrollState = rememberLazyListState()

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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    scrollState.animateScrollToItem(0)
                }
            }) {
                Icon(Icons.Filled.ArrowUpward, contentDescription = "Scroll to top")
            }
        }
    ) {
        Column {
            SearchBar(searchText.value, onSearchTextChange = { newText -> searchText.value = newText })

            LazyRow {
                items(categories) { category ->
                    Button(onClick = { /* Handle click */ }) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = "Most Recent",
                    modifier = Modifier.clickable { /* Handle click for Most Recent */ }
                )

                Text(
                    text = "Most Relevant",
                    modifier = Modifier.clickable { /* Handle click for Most Relevant */ }
                )
            }

            if (isLoading.value) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            } else {
                LazyColumn(state = scrollState) {
                    items(recipes.value.size) { index ->
                        RecipeCard(recipes.value[index]) {
                            // Handle recipe click
                        }
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

@Composable
fun TestCard() {
    val dishName = "Test Dish"
    val dishDescription = "This is a test dish description."
    val ingredients = listOf("Ingredient 1", "Ingredient 2", "Ingredient 3")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = dishName, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = dishDescription, style = MaterialTheme.typography.body1)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = "Ingredients:", style = MaterialTheme.typography.body1)
            ingredients.forEach { ingredient ->
                Text(text = "- $ingredient", style = MaterialTheme.typography.body2)
            }
        }
    }
}