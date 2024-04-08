package com.example.miams.ui.screens

import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.miams.LocalDB.Tables.Recipes
import com.example.miams.Types.RecipesLists
import androidx.navigation.NavController
import com.example.miams.LocalDB.RecipesDatabase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val placeHolder = RecipesLists(
    pk = 1551,
    title = "Slow Cooker Beef and Barley Soup",
    featured_image = "https://nyc3.digitaloceanspaces.com/food2fork/food2fork-static/featured_images/1551/featured_image.png",
    ingredients = listOf("8.8 ounces barley",
        "1 cup chopped celery",
        "1 pound stewing beef",
        "1 teaspoon kosher salt",
        "1 1/2 cups chopped onion",
        "1/2 teaspoon kosher salt",
        "1/2 cup all-purpose flour",
        "1 small jalapeno (optional)",
        "3 tablespoons minced garlic",
        "1/4 cup chopped fresh parsley",
        "2 cups sliced carrots, peeled",
        "2 cups sliced cremini mushrooms",
        "1/2 teaspoon ground black pepper",
        "64 ounces reduced sodium beef broth",
        "2-3 tablespoons Worcestershire Sauce",
        "3 tablespoons extra virgin olive oil",
        "1 medium zucchini, sliced and chopped",
        "1/2 teaspoon freshly ground black pepper",
        "2-3 tablespoons fresh chopped thyme leaves"
    )
)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(navController: NavController,pk: Int) {
    val database = RecipesDatabase.getInstance(LocalContext.current.applicationContext)
    val RecipesDAO = database.RecipesDAO()
    val DbRecipes = remember { mutableStateOf(Recipes()) }
    val DbRecipesL = remember { mutableStateOf(listOf<Recipes>()) }

    val isLoading = remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        suspend fun getRecipeDetails() {
            isLoading.value = true
            val recipe = RecipesDAO.getRecipeById(pk)
            DbRecipes.value = recipe
            isLoading.value = false
        }
        getRecipeDetails() // Call the suspend function here
    }

    fun getAllRecipes() {
        CoroutineScope(Dispatchers.Main).launch {
            isLoading.value = true
            val recipesList = RecipesDAO.getAllRecipes()
            DbRecipesL.value = recipesList
            isLoading.value = false
        }
    }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recipe Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Go back")
                    }
                }
            )
        }
    ){
        if (isLoading.value){
            Log.d("DetailRecipe", "Loading...")
            Text(text = "tetettee")
            CircularProgressIndicator()
        }else{
            Log.d("DetailRecipe", DbRecipes.value.toString())

            Column(modifier = Modifier.padding(8.dp)) {
                val bitmap = BitmapFactory.decodeByteArray(DbRecipes.value.image, 0, DbRecipes.value.image!!.size)
                Image(
                    painter = rememberImagePainter(bitmap),
                    contentDescription = "Recipe Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )


                Column(Modifier.padding(top = 10.dp, start = 30.dp)) {
                    Text(text = DbRecipes.value.title,fontSize = 30.sp)
                    Text(
                        text = "Ingrédient:",
                        fontSize = 15.sp,
                        modifier = Modifier
                            .padding(top = 15.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .padding(top = 5.dp, start = 15.dp)
                    ) {
                        val ingredients = Gson().fromJson(DbRecipes.value.ingredients, Array<String>::class.java).toList()
                        items(ingredients.size) { index ->
                            Text(
                                text = (index+1).toString()+"- "+ingredients[index],
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(top = 5.dp)
                            )
                        }

                    }
                }
            }

        }
    }


}
