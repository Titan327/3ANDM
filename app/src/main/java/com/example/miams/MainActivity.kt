package com.example.miams

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.miams.LocalDB.RecipesDatabase
import com.example.miams.LocalDB.Tables.Recipes
import com.example.miams.ui.screens.SearchScreen
import com.example.miams.ui.screens.SplashScreen
import com.example.miams.ui.theme.MiamsTheme
import com.example.miams.ui.screens.HomeScreen
import com.example.miams.ui.screens.RecipeDetailScreen
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MiamsTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                ) {
                    val navController = rememberNavController()
                    val startDestination = "splash"
                    NavHost(navController = navController, startDestination = startDestination) {
                        composable("splash") {
                            SplashScreen(navController)
                        }
                        composable("search") {
                            SearchScreen()
                        }
                        composable ("home"){
                            HomeScreen(navController)
                        }
                        composable("recipeDetail/{recipeId}") { backStackEntry ->
                            val database = RecipesDatabase.getInstance(applicationContext)
                            val recipesDAO = database.RecipesDAO()
                            val recipeId = backStackEntry.arguments?.getString("recipeId")

                            val coroutineScope = rememberCoroutineScope()
                            var recipe by remember { mutableStateOf<Recipes?>(null) }

                            LaunchedEffect(recipeId) {
                                coroutineScope.launch {
                                    recipe = recipeId?.let { recipesDAO.getRecipeById(it) }
                                }
                            }

                            if (recipe != null) {
                                RecipeDetailScreen(recipe!!, navController)
                            }

                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MiamsTheme {
        Greeting("Android")
    }
}}
