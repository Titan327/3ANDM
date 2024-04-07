package com.example.miams.ui.screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.platform.LocalContext
import com.example.miams.LocalDB.RecipesDatabase
import com.example.miams.LocalDB.Tables.Recipes
import com.example.miams.Types.RecipesLists
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val scope = rememberCoroutineScope()
    var searchText = remember { mutableStateOf("") }
    var SearchRecipes = remember { mutableStateOf(listOf<SearchResult>()) }
    var isLoading = remember { mutableStateOf(false) }
    val categories = listOf("Beef", "Chicken", "Dessert")
    val scrollState = rememberLazyListState()

    val database = RecipesDatabase.getInstance(LocalContext.current.applicationContext)
    val RecipesDAO = database.RecipesDAO()
    val DbRecipes = remember { mutableStateOf(listOf<Recipes>()) }

    var convertedRecipes: List<RecipesLists> = emptyList()

    fun getAllRecipes() {
        CoroutineScope(Dispatchers.Main).launch {
            isLoading.value = true
            val recipesList = RecipesDAO.getAllRecipes()
            DbRecipes.value = recipesList
            convertedRecipes = DbRecipesToRecipesList(DbRecipes.value)
            isLoading.value = false
        }
    }

    getAllRecipes()


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
                    Button(
                        onClick = { /* Handle click */ },
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        Text(
                            text = category,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                    //Spacer(modifier = Modifier.width(8.dp))
                }
            }


            if (isLoading.value) {
                CircularProgressIndicator()
            } else {
                // Affichez la LazyColumn si isLoading est faux
                LazyColumn {
                    items(convertedRecipes.size) { index ->
                        RecipeCard(convertedRecipes[index])
                    }
                }
            }



        }
    }
}

fun DbRecipesToRecipesList(DbRecipes:List<Recipes>): List<RecipesLists>{
    val data = DbRecipes.map { DbRecipes ->
        RecipesLists(
            title = DbRecipes.title,
            image = DbRecipes.image
        )
    }
    return data
}


@Composable
fun SearchBar(searchText: String, onSearchTextChange: (String) -> Unit) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        label = { Text("Search") },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(onClick = {  }) {
                Icon(Icons.Filled.Search, contentDescription = "Rechercher")
            }
        },
    )
}



@Composable
fun RecipeCard(recipe: RecipesLists) {
    val bitmap = BitmapFactory.decodeByteArray(recipe.image, 0, recipe.image!!.size)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            //.clickable(onClick = onClick)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            Image(
                painter = rememberImagePainter(bitmap),
                contentDescription = "Recipe Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(75.dp)
                    .width(75.dp)
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