package com.tufelmalik.dailykill.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tufelmalik.dailykill.data.classes.Constants.WHEATHER_API_KEY
import com.tufelmalik.dailykill.data.model.weather.WeatherModel
import com.tufelmalik.dailykill.data.utilities.ApiService

class WeatherRepository(val apiInstance : ApiService) {

    private val _wather =MutableLiveData<WeatherModel>()
    val weather : LiveData<WeatherModel> get() = _wather

    suspend fun getWeatherByCity(city: String) {
        val result = apiInstance.getWeatherByCity(city, WHEATHER_API_KEY)
        if (result.isSuccessful) {
            Log.d("WeatherActivity", "API Success: ${result.body()}")
            _wather.postValue(result.body())
        } else {
            Log.e("WeatherActivity", "API Error: ${result.code()}")
        }

    }


}