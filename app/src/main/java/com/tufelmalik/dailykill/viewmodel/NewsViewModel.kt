package com.tufelmalik.dailykill.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tufelmalik.dailykill.data.model.NewsModel
import com.tufelmalik.dailykill.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import retrofit2.Response

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    val indiaNews: LiveData<NewsModel>
        get() = repository.indiaNews
    val usaNews: LiveData<NewsModel>
        get() = repository.usaNews


    init {
        fetchNewsData()
    }

    private fun fetchNewsData() {
    viewModelScope.launch(Dispatchers.IO) {
        repository.getAllIndiaNews()
        repository.getAllUSANews()
    }

    }
}