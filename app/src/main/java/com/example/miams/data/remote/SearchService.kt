package com.example.miams.data.remote

import com.example.miams.data.remote.dto.SearchResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android

import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*


interface SearchService {

    suspend fun getSearchResult(): List<SearchResponse>

    companion object {
        fun create(): SearchService {
            return SearchServiceImpl(
                client = HttpClient(Android){
                    install(ContentNegotiation){
                        json()
                    }
                }
            )
        }
    }

}