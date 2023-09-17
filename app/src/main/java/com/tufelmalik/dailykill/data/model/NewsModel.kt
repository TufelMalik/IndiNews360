package com.tufelmalik.dailykill.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

//@Serializable

@Entity(tableName = "news")
data class NewsModel(
    @PrimaryKey(autoGenerate = true)
    val coinId : Int? = null,
    val articles: List<Article>,
    val status: String,
    val totalResults: Int,
    val isFavroite : Boolean
)