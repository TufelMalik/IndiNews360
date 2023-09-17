package com.tufelmalik.dailykill.data.classes

import androidx.lifecycle.LiveData
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tufelmalik.dailykill.data.model.NewsModel

interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFavCoins(newsModel: NewsModel)

    @Query("SELECT * FROM news")
    fun getAllSavedNews() : LiveData<List<NewsModel>>

}