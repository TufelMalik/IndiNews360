package com.tufelmalik.dailykill.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tufelmalik.dailykill.data.classes.Constants.WEATHER_BASE_URL
import com.tufelmalik.dailykill.data.classes.Constants.WHEATHER_API_KEY
import com.tufelmalik.dailykill.data.model.weather.WeatherModel
import com.tufelmalik.dailykill.data.utilities.ApiService

class WeatherRepository(val apiInstance : ApiService) {

    private val _weather =MutableLiveData<WeatherModel>()
    val weather : LiveData<WeatherModel> get() = _weather

    suspend fun getWeatherByCity(city: String) {
        val result = apiInstance.getWeatherByCityName(city, "3fabf25f0f20341ba60e8d93ed394822")
        if (result.isSuccessful) {
            Log.d("WeatherActivity", "API Success: ${result.body()}")
            _weather.postValue(result.body())
        } else {
            Log.e("WeatherActivity", "API Error: ${result.code()}")
        }
    }



}