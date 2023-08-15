package com.tufelmalik.dailykill.data.utilities


import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.model.NewsModel
import retrofit2.Response
import retrofit2.http.GET


interface ApiService {

    @GET(Constants.INDIA_KEY)
    suspend fun getAllIndianNews(): Response<NewsModel>

    @GET(Constants.USA_KEY)
    suspend fun getAllUSANews(): Response<NewsModel>
}
