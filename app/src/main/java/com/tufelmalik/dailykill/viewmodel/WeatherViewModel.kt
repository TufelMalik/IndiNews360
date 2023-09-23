package com.tufelmalik.dailykill.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tufelmalik.dailykill.data.model.weather.WeatherModel
import com.tufelmalik.dailykill.data.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(val repository: WeatherRepository) : ViewModel() {


    private val _weather = MutableLiveData<WeatherModel>()
    val weather: LiveData<WeatherModel> get() = _weather

    val weatherVM : LiveData<WeatherModel>
        get() = repository.weather

    suspend fun fetchWeather(city: String) {
        viewModelScope.launch(Dispatchers.IO) {
           repository.getWeatherByCity(city)
        }
    }

//
//    suspend fun getSavedWeatherData(){
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getAllSavedWeatherData()
//        }
//    }
//
//    suspend fun saveWeatherData(weatherModel: WeatherModel){
//        viewModelScope.launch(Dispatchers.IO){
//            repository.saveWeatherData(weatherModel)
//        }
//    }




}