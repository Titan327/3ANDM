package com.example.miams.http.repository

import android.util.Log
import com.example.miams.http.AppHttpClient
import com.example.miams.http.types.SearchResponse
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers

class RecipeRepository {
    companion object{
        private val client = AppHttpClient.getClient()
        private const val API_URL = "https://food2fork.ca/api/recipe"
    }

    suspend fun getSearchResult(page: Int, query: String): SearchResponse? {
        try {
            val response = client.get("$API_URL/search/?page=$page&query=$query") {
                headers {
                    append("Authorization", "Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
                }
            }

            if (response.status.value in 200..299) {
                return response.body()
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

    suspend fun getSearchResultByUrl(url: String): SearchResponse? {
        try {
            val response = client.get(url) {
                headers {
                    append("Authorization", "Token 9c8b06d329136da358c2d00e76946b0111ce2c48")
                }
            }


            if (response.status.value in 200..299) {
                return response.body()
            } else {
                return null
            }
        } catch (e: Exception) {
            return null
        }
    }

}