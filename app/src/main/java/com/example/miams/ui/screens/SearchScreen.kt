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
import com.example.miams.http.repository.RecipeRepository
import com.example.miams.http.types.SearchResponse
import kotlinx.coroutines.launch

@Composable
fun SearchScreen(){
    val scope = rememberCoroutineScope()
    val search = remember { mutableStateOf<SearchResponse?>(null) }

    LaunchedEffect(true) {
        scope.launch {
            try {
                search.value = RecipeRepository().getSearchResult(1, "beef")
                Log.d("test","on est la")

            } catch (e: Exception) {
                Log.e("SearchScreen", "Error while getting search result for recipe", e)
            }
        }
    }

    if (search.value != null) {

        LazyColumn {
            items(search.value!!.results.size) { item ->
                val item = search.value!!.results[item]

                Column(
                    modifier = Modifier.padding(15.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(
                            data = item.featured_image
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .padding(bottom = 5.dp)
                    )
                    Text(
                        text = item.title,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        fontSize = 20.sp

                    )
                }

            }
        }



    } else {
        Text(text = "Loading search...")
    }
}