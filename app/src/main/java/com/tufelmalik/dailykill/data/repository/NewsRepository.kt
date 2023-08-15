package com.tufelmalik.dailykill.data.repository

import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.model.NewsModel
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.data.utilities.ApiService

class NewsRepository(private val apiInterface: ApiService) {

    suspend fun getAllIndiaNews(): NewsModel? {
        val response = apiInterface.getAllIndianNews()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }

    suspend fun getAllUSANews(): NewsModel? {
        val response = apiInterface.getAllUSANews()
        return if (response.isSuccessful) {
            response.body()
        } else {
            null
        }
    }
}