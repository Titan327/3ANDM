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
import androidx.compose.foundation.lazy.rememberLazyListState import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.miams.http.repository.RecipeRepository
import com.example.miams.http.types.SearchResponse
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.navigation.NavController
import com.example.miams.LocalDB.RecipesDatabase
import com.example.miams.LocalDB.Tables.Recipes
import com.example.miams.Types.RecipesLists
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import java.net.URL


var convertedRecipes by mutableStateOf<List<RecipesLists>>(emptyList())
var nextUrl = "https://food2fork.ca/api/recipe/search/?page=2&query="
val isLoading = MutableStateFlow(false)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var searchText = remember { mutableStateOf("") }
    var SearchResponse = remember { mutableStateOf(SearchResponse) }
    //var isLoading = remember { mutableStateOf(false) }
    val isLoading by isLoading.collectAsState()
    var newLoad = remember { mutableStateOf(false) }
    val categories = listOf("Beef", "Chicken", "Dessert")
    val scrollState = rememberLazyListState()


    val database = RecipesDatabase.getInstance(LocalContext.current.applicationContext)
    val RecipesDAO = database.RecipesDAO()
    val DbRecipes = remember { mutableStateOf(listOf<Recipes>()) }

    val search = remember { mutableStateOf<SearchResponse?>(null) }

    val coroutineScope = rememberCoroutineScope()

    //var convertedRecipes: List<RecipesLists> = emptyList()
    //var convertedRecipes by remember { mutableStateOf<List<RecipesLists>>(emptyList()) }

    fun getAllRecipes() {
        CoroutineScope(Dispatchers.Main).launch {
            changeStateLoading(true)
            val recipesList = RecipesDAO.getAllRecipes()
            DbRecipes.value = recipesList
            convertedRecipes = DbRecipesToRecipesList(DbRecipes.value)
            changeStateLoading(false)
        }
    }
    LaunchedEffect(Unit) {
        getAllRecipes()
    }


    fun urlToByteArray(url: String): ByteArray {
        val inputStream = URL(url).openStream()
        val bytes = inputStream.readBytes()
        inputStream.close()
        return bytes
    }

    suspend fun onAddRecipes(id: Int,title: String, url: String, ingredients: List<String>) {
        withContext(Dispatchers.IO) {
            val ingredientsString = Gson().toJson(ingredients)
            RecipesDAO.upsertRecipes(Recipes(
                id = id,
                title = title,
                image = urlToByteArray(url),
                ingredients = ingredientsString
            ))
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
            //SearchBar(searchText.value, onSearchTextChange = { searchText.value = it })
            SearchBar()


            LazyRow {
                items(categories) { category ->
                    Button(

                        onClick = {
                            changeStateLoading(true)
                            scope.launch {
                                try {
                                    search.value = RecipeRepository().getSearchResult(1, category)
                                    if (search.value != null) {
                                        nextUrl = search.value!!.next.toString()
                                        search.value!!.results.forEach{ result ->
                                            onAddRecipes(
                                                result.pk,
                                                result.title,
                                                result.featured_image,
                                                result.ingredients
                                            )
                                        }
                                        val data = SearchResponseToRecipesList(search.value!!)
                                        convertedRecipes = data
                                        changeStateLoading(false)
                                    }
                                } catch (e: Exception) {
                                    Log.e("SearchScreen", "Error while getting search result for recipe", e)
                                }
                            }
                        },
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


            if (isLoading) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (convertedRecipes.isNotEmpty()) {

                    newLoad.value = false

                    LazyColumn(state = scrollState) {
                        var url =
                        items(convertedRecipes.size) { index ->
                            RecipeCard(convertedRecipes[index],navController)
                            if (index == (convertedRecipes.size-5)){

                                newLoad.value = false

                                this@LazyColumn.item {

                                    Row(
                                        modifier = Modifier.fillMaxSize(),
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        CircularProgressIndicator()
                                    }

                                }

                                LaunchedEffect(coroutineScope) {
                                    try {
                                        Log.d("SearchScreen", nextUrl.toString())
                                        search.value = RecipeRepository().getSearchResultByUrl(nextUrl)
                                        Log.d("SearchScreen", search.value.toString())
                                        if (search.value != null) {
                                            nextUrl = search.value!!.next.toString()
                                            search.value!!.results.forEach{ result ->
                                                onAddRecipes(
                                                    result.pk,
                                                    result.title,
                                                    result.featured_image,
                                                    result.ingredients
                                                )
                                            }
                                            val data = SearchResponseToRecipesList(search.value!!)
                                            convertedRecipes = convertedRecipes + data
                                            Log.d("SearchScreen", convertedRecipes.toString())
                                        }
                                    } catch (e: Exception) {
                                        Log.e("SearchScreen", "Error while getting search result for recipe", e)
                                    }
                                }
                                newLoad.value = false

                            }
                        }
                    }



                }else{
                    Text("No recipes found.")
                }
            }

        }
    }
}

fun changeStateLoading(state:Boolean) {
    isLoading.value = state
}

fun DbRecipesToRecipesList(DbRecipes:List<Recipes>): List<RecipesLists>{
    val data = DbRecipes.map { DbRecipes ->
        RecipesLists(
            pk = DbRecipes.id,
            title = DbRecipes.title,
            image = DbRecipes.image,
            ingredients = Gson().fromJson(DbRecipes.ingredients, Array<String>::class.java).toList()
        )
    }
    return data
}

fun SearchResponseToRecipesList(SearchResponse:SearchResponse): List<RecipesLists>{
    val data = SearchResponse.results.map { SearchResponse ->
        RecipesLists(
            pk = SearchResponse.pk,
            title = SearchResponse.title,
            featured_image = SearchResponse.featured_image,
            ingredients = SearchResponse.ingredients
        )
    }
    return data
}



@Composable
fun SearchBar() {
    val scope = rememberCoroutineScope() // Create a CoroutineScope
    var text by remember { mutableStateOf("") }
    val search = remember { mutableStateOf<SearchResponse?>(null) }
    val focusManager = LocalFocusManager.current

    val database = RecipesDatabase.getInstance(LocalContext.current.applicationContext)
    val RecipesDAO = database.RecipesDAO()

    fun urlToByteArray(url: String): ByteArray {
        val inputStream = URL(url).openStream()
        val bytes = inputStream.readBytes()
        inputStream.close()
        return bytes
    }

    suspend fun onAddRecipes(id: Int,title: String, url: String, ingredients: List<String>) {
        withContext(Dispatchers.IO) {
            val ingredientsString = Gson().toJson(ingredients)
            RecipesDAO.upsertRecipes(Recipes(
                id = id,
                title = title,
                image = urlToByteArray(url),
                ingredients = ingredientsString
            ))
        }
    }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth(),
        label = { Text("Search...") },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                changeStateLoading(true)
                focusManager.clearFocus()
                scope.launch {
                    try {
                        search.value = RecipeRepository().getSearchResult(1, text)
                        if (search.value != null) {
                            val data = SearchResponseToRecipesList(search.value!!)
                            search.value!!.results.forEach{ result ->
                                onAddRecipes(
                                    result.pk,
                                    result.title,
                                    result.featured_image,
                                    result.ingredients
                                )
                            }
                            convertedRecipes = data
                            changeStateLoading(false)
                            Log.d("SearchScreen", data.toString())
                        }
                    } catch (e: Exception) {
                        Log.e("SearchScreen", "Error while getting search result for recipe", e)
                    }
                }


            }
        )

    )
}


@Composable
fun RecipeCard(recipe: RecipesLists,navController: NavController) {

    val id = recipe.pk

    Log.d("DetailRecipe", recipe.pk.toString())

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = { navController.navigate("detail/$id") })
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {

            if (recipe.image != null){
                val bitmap = BitmapFactory.decodeByteArray(recipe.image, 0, recipe.image!!.size)
                Image(
                    painter = rememberImagePainter(bitmap),
                    contentDescription = "Recipe Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(75.dp)
                        .width(75.dp)
                )
            }else{
                Image(
                    painter = rememberImagePainter(data = recipe.featured_image),
                    contentDescription = "Recipe Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(75.dp)
                        .width(75.dp)
                )
            }

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