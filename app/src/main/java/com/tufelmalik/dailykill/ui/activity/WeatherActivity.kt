package com.tufelmalik.dailykill.ui.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.model.weather.Main
import com.tufelmalik.dailykill.data.repository.WeatherRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.ActivityWeatherBinding
import com.tufelmalik.dailykill.viewmodel.WeatherVMFactory
import com.tufelmalik.dailykill.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.format.TextStyle
import java.util.Date
import java.time.LocalDate
import java.util.Locale

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

        binding.weatherProgressBar.isVisible = true

        viewModel.weatherVM.observe(this) { weatherModel ->
            if(weatherModel !=  null){
                binding.weatherProgressBar.isVisible = false
                setBgAccordingToWeather(weatherModel.weather[0].main)
                binding.txtCityName.text = weatherModel.name
                binding.txtWheatherDate.text = getCurrentDateInFormat()
                binding.txtWheatherDay.text = getCurrentDayName()
                binding.txtHumidity.text = weatherModel.main.humidity.toString()
                binding.txtWheatherName.text = weatherModel.weather[0].main
                binding.txtWindSpeed.text = weatherModel.wind.speed.toString()
                binding.txtConsditions.text = weatherModel.rain._1h.toString()
                binding.txtSunrise.text = weatherModel.sys.sunrise.toString()
                binding.txtSunset.text = weatherModel.sys.sunset.toString()
                binding.txtSea.text = weatherModel.main.sea_level.toString()
                val celsiusCharSequence: CharSequence = convertRawTempToActualTemp(weatherModel.main.temp).toString()
                binding.txtTemprature.text = celsiusCharSequence
                Log.d("WeatherActivity", "\n\n\n\n...\n\n\n\n\n....n\n\nWind Speed : " + weatherModel.wind.speed.toString())
            }else{
                Toast.makeText(this@WeatherActivity,"Something went wrong !!!",Toast.LENGTH_SHORT).show()
            }
        }
        getWeatherData()
    }

    private fun setBgAccordingToWeather(data: String) {
        val main = data.lowercase()
        if(main.equals("rain")){
            binding.wheatherAnimation.setAnimation(R.raw.rain_animation)
            binding.weatherActivity.setBackgroundResource(R.drawable.rain_background)
        }else if(main.equals("sunny")){
            binding.wheatherAnimation.setAnimation(R.raw.sunny_animation)
            binding.weatherActivity.setBackgroundResource(R.drawable.sunny_background)
        }else if(main.equals("cloud")){
            binding.wheatherAnimation.setAnimation(R.raw.cloud_animaton)
            binding.weatherActivity.setBackgroundResource(R.drawable.colud_background)
        }else if(main.equals("snow")){
            binding.wheatherAnimation.setAnimation(R.raw.cloud_animaton)
            binding.weatherActivity.setBackgroundResource(R.drawable.snow_background)
        }else{
            binding.wheatherAnimation.setAnimation(R.raw.sunny_animation)
            binding.weatherActivity.setBackgroundResource(R.drawable.sunny_background)
        }
    }

    fun getCurrentDateInFormat(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    fun getCurrentDayName(): String {
        val currentDate = LocalDate.now()
        val dayOfWeek = currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return dayOfWeek
    }

    fun convertRawTempToActualTemp(kelvinTemperature: Double): String {
        var temp = kelvinTemperature - 273.15
        return temp.toInt().toString()

    }



    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@WeatherActivity,MainActivity::class.java))
    }
    private fun getWeatherData() {
        lifecycleScope.launch {
            viewModel.fetchWeather("Bharuch")
        }
    }
}
