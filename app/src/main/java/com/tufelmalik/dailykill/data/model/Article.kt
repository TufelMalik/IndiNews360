package com.tufelmalik.dailykill.data.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class Article(
    val author: String,
    val content: String,
    val description: String,
    val publishedAt: String,
    val source: List<Source>,
    val title: String,
    val url: String,
    val urlToImage: String
)