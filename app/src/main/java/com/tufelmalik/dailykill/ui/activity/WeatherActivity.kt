package com.tufelmalik.dailykill.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.tufelmalik.dailykill.data.repository.WeatherRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.ActivityWeatherBinding
import com.tufelmalik.dailykill.viewmodel.WeatherVMFactory
import com.tufelmalik.dailykill.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private val apiInstance = ApiInstance.weatherApi
    private val weatherRepo = WeatherRepository(apiInstance)
    private val viewModel: WeatherViewModel by viewModels {
        WeatherVMFactory(weatherRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)



//        viewModel.weatherVM.observe(this) { weatherModel ->
//            binding.txtTemprature.text = weatherModel.main.temp.toString()
//            binding.txtSunset.text = weatherModel.sys.sunset.toString()
//            binding.txtSunrise.text = weatherModel.sys.sunrise.toString()
//            Log.d("WeatherActivity", "Wind Speed : " + weatherModel.wind.speed.toString())
//        }
//        getData()
    }

    private fun getData() {
        lifecycleScope.launch {
            viewModel.fetchWeather("Bharuch")
        }
    }
}
