package com.example.miams.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.miams.LocalDB.RecipesDatabase
import com.example.miams.LocalDB.Tables.Recipes
import com.example.miams.R
import com.example.miams.ui.theme.Emerald
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SplashScreen() {
    val database = RecipesDatabase.getInstance(LocalContext.current.applicationContext)
    val RecipesDAO = database.RecipesDAO()
    val recipes = remember { mutableStateOf(listOf<Recipes>()) }

    fun onAddRecipes() {
        CoroutineScope(Dispatchers.IO).launch {
            RecipesDAO.upsertRecipes(Recipes(title = "TestTest"))
        }
    }

    fun getAllRecipes() {
        CoroutineScope(Dispatchers.Main).launch {
            val recipesList = RecipesDAO.getAllRecipes()
            recipes.value = recipesList
        }
    }

    getAllRecipes()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Emerald),
        contentAlignment = Alignment.Center


    ) {
        
        recipes.value.forEach{ recipe ->
            Text(text = recipe.title)
        }
        
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