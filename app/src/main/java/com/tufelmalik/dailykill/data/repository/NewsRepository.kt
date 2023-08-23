package com.tufelmalik.dailykill.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.model.NewsModel
import com.tufelmalik.dailykill.data.model.weather.WeatherModel
import com.tufelmalik.dailykill.data.utilities.ApiService

class NewsRepository(private val apiInterface: ApiService) {
    private val _indiaNews = MutableLiveData<NewsModel>()
    private val _usaNews = MutableLiveData<NewsModel>()
    private val _category = MutableLiveData<String>()

    val indiaNews: LiveData<NewsModel> get() = _indiaNews
    val usaNews: LiveData<NewsModel> get() = _usaNews
    val category : LiveData<String> get() = _category


    suspend fun getIndianNewsByCategory(category: String) {
        val result =  apiInterface.getIndianNewsByCategory("in",category,Constants.NEWS_API_KEY)
        if(result?.body() != null){
            _category.postValue(category)
            _indiaNews.postValue(result.body())
        }
    }


    suspend fun getAllIndiaNews() {
        val result = apiInterface.getAllIndianNews()
        if (result?.body() != null) {
            _indiaNews.postValue(result.body())
        }
    }

    suspend fun getAllUSANews() {
        val result = apiInterface.getAllUSANews()
        if (result?.body() != null) {
            _usaNews.postValue(result.body())
        }
    }
}
