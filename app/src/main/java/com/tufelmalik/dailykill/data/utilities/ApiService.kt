package com.tufelmalik.dailykill.data.utilities


import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.classes.Constants.WHEATHER_API_KEY
import com.tufelmalik.dailykill.data.model.NewsModel
import com.tufelmalik.dailykill.data.model.weather.WeatherModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiService {

    @GET(Constants.INDIA_KEY)
    suspend fun getAllIndianNews(): Response<NewsModel>


    @GET("top-headlines")
    suspend fun getIndianNewsByCategory(
        @Query("country") country: String = "in",
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = "f07962ab9a4e4883be33443776ac7e3a"
    ): Response<NewsModel>


    @GET(Constants.USA_KEY)
    suspend fun getAllUSANews(): Response<NewsModel>


    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") appid: String = WHEATHER_API_KEY
    ): Response<WeatherModel>




}
