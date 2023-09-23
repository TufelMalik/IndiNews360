package com.tufelmalik.dailykill.data.repository

import WeatherDao
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tufelmalik.dailykill.data.model.weather.WeatherModel
import com.tufelmalik.dailykill.data.utilities.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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

//    suspend fun getAllSavedWeatherData() {
//        val savedWeather = weatherDao.getSavedWeather().value
//        if (savedWeather != null && savedWeather.isNotEmpty()) {
//             CoroutineScope(Dispatchers.IO).launch {
//                _weather.postValue(savedWeather[0])
//            }
//        } else {
//            fetchWeatherDataFromApi()
//        }
//    }
//
//    private suspend fun fetchWeatherDataFromApi() {
//        try {
//            val response = apiInstance.getWeatherByCityName("Bharuch","3fabf25f0f20341ba60e8d93ed394822") // Replace with your API call
//            if (response.isSuccessful) {
//                val weatherModel = response.body()
//                if (weatherModel != null) {
//                    // Save data to the database
//                    weatherDao.saveWeather(weatherModel)
//                    // Post the data to LiveData
//                    _weather.postValue(weatherModel!!)
//                } else {
//                    Log.e("WeatherRepository", "API response body is null")
//                }
//            } else {
//                Log.e("WeatherRepository", "API response not successful: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            Log.e("WeatherRepository", "API request failed: ${e.message}")
//        }
//    }

//    suspend fun saveWeatherData(weatherModel: WeatherModel){
//        weatherDao.saveWeather(weatherModel)
//    }



}