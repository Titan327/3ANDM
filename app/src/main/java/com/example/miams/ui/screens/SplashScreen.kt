package com.example.miams.ui.screens

import androidx.navigation.compose.NavHost
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.miams.LocalDB.RecipesDatabase
import com.example.miams.LocalDB.Tables.Recipes
import com.example.miams.R
import com.example.miams.http.repository.RecipeRepository
import com.example.miams.http.types.SearchResponse
import com.example.miams.ui.theme.Emerald
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.URL
import java.sql.Blob


@Composable

fun SplashScreen(navController: NavHostController) {

    val database = RecipesDatabase.getInstance(LocalContext.current.applicationContext)
    val RecipesDAO = database.RecipesDAO()
    val recipes = remember { mutableStateOf(listOf<Recipes>()) }

    val scope = rememberCoroutineScope()
    val search = remember { mutableStateOf<SearchResponse?>(null) }

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

    fun deleteAllRecipes() {
        CoroutineScope(Dispatchers.IO).launch {
            RecipesDAO.deleteAllRecipes()
        }
    }


    LaunchedEffect(true) {
        scope.launch {
            try {
                search.value = RecipeRepository().getSearchResult(1, "")
                if (search.value != null) {
                    deleteAllRecipes()

                    search.value!!.results.forEach{ result ->
                        onAddRecipes(
                            result.pk,
                            result.title,
                            result.featured_image,
                            result.ingredients
                        )

                    }
                }
                navController.navigate("home")

            } catch (e: Exception) {
                Log.e("SearchScreen", "Error while getting search result for recipe", e)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Emerald),
        contentAlignment = Alignment.Center
    ) {

        Logo()

    }
}

@Composable
fun Logo(modifier: Modifier = Modifier) {
    val rotation = remember { Animatable(0f) }
    LaunchedEffect(key1 = Unit) {
        rotation.animateTo(360f,
            animationSpec = infiniteRepeatable(
                tween(durationMillis = 1000, easing = LinearEasing)
            )
        )
    }
    Image(
        painter = painterResource(id = R.drawable.ic_miams_logo),
        contentDescription = "Logo",
        modifier = modifier
            .size(200.dp)
            .rotate(rotation.value)
    )
}