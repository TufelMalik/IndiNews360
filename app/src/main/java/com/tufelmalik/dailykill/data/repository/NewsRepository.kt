package com.tufelmalik.dailykill.data.repository

import com.tufelmalik.dailykill.common.Constants
import com.tufelmalik.dailykill.common.`class`.KtorClient
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.model.NewsModel
import io.ktor.client.request.get

class NewsRepository  {
    val apiLink = Constants.api_link

    suspend fun getAllNews() : List<Article> = KtorClient.httpClient.get(apiLink)


}