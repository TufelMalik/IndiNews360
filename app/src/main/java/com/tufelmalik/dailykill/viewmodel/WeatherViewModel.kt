package com.tufelmalik.dailykill.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tufelmalik.dailykill.data.model.weather.WeatherModel
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {

    val weatherVM : LiveData<WeatherModel>
        get() = repository.weather

    suspend fun fetchWeatherData(city: String) {
        repository.getWeatherByCity(city)
    }



}