package com.tufelmalik.dailykill.viewmodel

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.model.NewsModel
import com.tufelmalik.dailykill.data.repository.NewsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {



    val indiaNews: LiveData<NewsModel>
        get() = repository.indiaNews
    val usaNews: LiveData<NewsModel>
        get() = repository.usaNews

    init {
        fetchNewsData()
    }


    suspend fun getIndianNewsByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getIndianNewsByCategory(category)
        }
    }
    fun fetchNewsData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllIndiaNews()
            repository.getAllUSANews()
        }
    }
    fun getAllIndiaNews() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllIndiaNews()
        }
    }
    fun changeTabBg(selectedCategory: Int, tabGroupNf: RadioGroup) {
        for (i in 0 until tabGroupNf.childCount) {
            val radioButton = tabGroupNf.getChildAt(i) as RadioButton
            if (radioButton.id != selectedCategory) {
                radioButton.setBackgroundResource(R.drawable.unselected_tab_bg)
            } else {
                radioButton.setBackgroundResource(R.drawable.selected_tab_bg)
            }
        }
    }

    fun checkUserNetworkState(context : Context) : Pair<Boolean,String> {
        return if (Constants.isOnline(context)) {
            Pair(true,"Online")
        } else {
            Pair(false,"Offline")
        }
    }

}
