package com.tufelmalik.dailykill.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.model.NewsModel
import com.tufelmalik.dailykill.data.repository.NewsRepository
import kotlinx.coroutines.launch


class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    val indiaNewsLiveData = MutableLiveData<NewsModel?>()
    val usaNewsLiveData = MutableLiveData<NewsModel?>()

    init {
        fetchNewsData()
    }

    private fun fetchNewsData() {
        viewModelScope.launch {
            val indiaNews = repository.getAllIndiaNews()
            indiaNewsLiveData.postValue(indiaNews)

            val usaNews = repository.getAllUSANews()
            usaNewsLiveData.postValue(usaNews)
        }
    }
}
