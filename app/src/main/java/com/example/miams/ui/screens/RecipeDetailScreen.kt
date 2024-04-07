package com.example.miams.ui.screens


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.miams.LocalDB.Tables.Recipes
import com.example.miams.Types.RecipesLists
@Composable
fun RecipeDetailScreen(recipe: Recipes) {
    val bitmap = BitmapFactory.decodeByteArray(recipe.image, 0, recipe.image!!.size)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Image(
                painter = rememberImagePainter(bitmap),
                contentDescription = "Recipe Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
            )
            Text(recipe.title, Modifier.padding(start = 8.dp))
        }
    }
}

@Composable
fun RecipeCard(recipe: RecipesLists, onClick: () -> Unit) {
    val bitmap = BitmapFactory.decodeByteArray(recipe.image, 0, recipe.image!!.size)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick)
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