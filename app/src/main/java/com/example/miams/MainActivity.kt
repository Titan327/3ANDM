package com.example.miams

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.miams.data.remote.SearchService
import com.example.miams.data.remote.SearchServiceImpl
import com.example.miams.data.remote.dto.SearchResponse
import com.example.miams.ui.theme.MiamsTheme


class MainActivity : ComponentActivity() {

    private val service = SearchService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent{

            val result = produceState<List<SearchResponse>> (
                initialValue = emptyList(),
                producer = {
                    value = service.getSearchResult()
                }
            )


            MiamsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    LazyColumn {
                        itemsIndexed(result.value) { index, searchResponse ->
                            Text(text = searchResponse.toString())
                        }

                    }

                }
            }

        }
    }
}

