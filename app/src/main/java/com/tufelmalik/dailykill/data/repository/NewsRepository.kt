package com.tufelmalik.dailykill.data.repository

import com.tufelmalik.dailykill.common.Constants
import com.tufelmalik.dailykill.common.`class`.KtorClient
import com.tufelmalik.dailykill.data.model.Article
import io.ktor.client.call.receive
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class NewsRepository {

    private val apiLink = Constants.api_link

    suspend fun getAllNews(): List<Article> {
        return withContext(Dispatchers.IO) {
            val httpClient = KtorClient.httpClient

            try {
                val response: HttpResponse = httpClient.get(apiLink)

                // Deserialize the JSON response using response.body()
                val responseBody = response.receive<String>()
                if (responseBody.isNotEmpty()) {
                    return@withContext Json.decodeFromString<List<Article>>(responseBody)
                } else {
                    // Handle empty response body
                    return@withContext emptyList()
                }
            } catch (e: Exception) {
                // Handle API request or JSON deserialization errors
                return@withContext emptyList()
            } finally {
                httpClient.close()
            }
        }
    }
}
