package com.example.miams.data.remote

import com.example.miams.data.remote.dto.SearchResponse

import io.ktor.client.*
import io.ktor.client.call.body
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.request.*

class SearchServiceImpl(
    private val client: HttpClient
) : SearchService {

    override suspend fun getSearchResult(): List<SearchResponse> {
        return try {
            client.get {
                url("https://food2fork.ca/api/recipe/search/")
            }.body()
        } catch (e: Exception) {
            println(e)
            emptyList()
        }
    }
}
