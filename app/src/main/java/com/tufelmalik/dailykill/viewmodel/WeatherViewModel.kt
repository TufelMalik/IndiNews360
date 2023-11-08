package com.tufelmalik.dailykill.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
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


    fun showBannerAds(context : Context, mAdView: AdView) {
        val adView = AdView(context)
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }


}