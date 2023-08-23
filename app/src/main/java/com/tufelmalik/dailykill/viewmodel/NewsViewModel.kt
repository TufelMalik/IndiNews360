package com.tufelmalik.dailykill.viewmodel

import android.content.Context
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

//    private val _selectedCategory = MutableLiveData<String>()
    val selectedCategory: LiveData<String>
        get() = repository.category

    init {
        fetchNewsData()
    }


    fun setSelectedCategory(category: String) {
       // value = category
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

    fun checkUserNetworkState(context : Context) : Pair<Boolean,String> {
        if (Constants.isOnline(context)) {
            return Pair(true,"Online")
        } else {
           return Pair(false,"Offline")
        }
    }

}
