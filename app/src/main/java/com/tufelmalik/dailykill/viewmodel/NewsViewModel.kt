package com.tufelmalik.dailykill.viewmodel

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants.isOnline
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.model.NewsModel
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.ui.adapter.NewsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale




class NewsViewModel(private val repository: NewsRepository) : ViewModel() {



    val indiaNews: LiveData<NewsModel>
        get() = repository.indiaNews
    val usaNews: LiveData<NewsModel>
        get() = repository.usaNews


    init {
        viewModelScope.launch {
            fetchNewsData()
        }
    }



    fun checkUserNetworkState(context: Context): Pair<Boolean, String> {
        if (isOnline(context)) {
            Toast.makeText(context, "Online", Toast.LENGTH_SHORT).show()
            return Pair(true, "Online")
        } else {
            Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show()
            return Pair(false, "Offline")
        }
    }



    suspend fun getIndianNewsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getIndianNewsByCategory(category)
        }
    }
    suspend fun fetchNewsData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllIndiaNews()
            repository.getAllUSANews()
        }
    }

    fun updateRecyclerView(searchText: String, newsList: List<Article>, newsAdapter: NewsAdapter) {
        val filteredNews = newsList.filter { article ->
            article.title?.lowercase(Locale.getDefault())?.contains(searchText) == true ||
                    article.description?.lowercase(Locale.getDefault())?.contains(searchText) == true ||
                    article.author?.lowercase(Locale.getDefault())?.contains(searchText) == true
        }
        newsAdapter.updateData(filteredNews)
    }

    fun changeTabBg(selectedCategory: Int, tabGroupNf: RadioGroup) {
        val radioGroup = tabGroupNf
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            if (radioButton.id != selectedCategory) {
                radioButton.setBackgroundResource(R.drawable.unselected_tab_bg)
            } else {
                radioButton.setBackgroundResource(R.drawable.selected_tab_bg)
            }
        }
    }



}
