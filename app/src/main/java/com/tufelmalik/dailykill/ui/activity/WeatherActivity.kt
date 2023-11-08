package com.tufelmalik.dailykill.ui.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.repository.WeatherRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.ActivityWeatherBinding
import com.tufelmalik.dailykill.viewmodel.WeatherVMFactory
import com.tufelmalik.dailykill.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private var selectedCityName = ""
    private lateinit var mAdView : AdView
    private val apiInstance = ApiInstance.weatherApi
    private val weatherRepo: WeatherRepository by lazy {
        WeatherRepository(apiInstance )
    }
    private val viewModel: WeatherViewModel by viewModels {
        WeatherVMFactory(weatherRepo)
    }

    private val citiesList = listOf("Agwar", "Agra", "Ahmedabad", "Alīgarh", "Ambattūr", "Amritsar", "Babura", "Bali", "Bansbaria", "Barakpur", "Belgharia", "Bhalswa Jahangirpur", "Bhatpara", "Bhayandar", "Bilaspur", "Bhopal", "Bommayapalaiyam", "Byatarayanpur", "Cachohalli", "Chandannagar", "Chandīgarh", "Chakapara", "Chinnasekkadu", "Chinchvad", "Dhanbad", "Harchandi", "Hariladih", "Hesarghatta", "Herohalli", "Hugli", "Hyderabad", "Jabalpur", "Jalhalli", "Jamshedpur", "Jethuli", "Jodhpur", "Kalamboli", "Kalkaji Devi", "Kammanhalli", "Kasgatpur", "Kendraparha", "Kodagihalli", "Kota", "Krishnanagar", "Kūkatpalli", "Ludhiana", "Madavaram", "Madavar", "Madhavaram", "Mahuli", "Manali", "Meerut", "Mirzapur", "Mirchi", "Muzaffarpur", "Nagpur", "Najafgarh", "Nangloi Jat", "Nasik", "Nathupur", "New Delhi", "Nerkunram", "Puducherry", "Pallavaram", "Pakri", "Prayagraj", "Puri", "Punadih", "Raipur", "Ranchi", "Salua", "Salt Lake City", "Saino", "Santoshpur", "Secunderabad", "Shekhpura", "Shimla", "Shrīrampur", "Simli Murarpur", "Sijua", "Sonawan", "Sondekoppa", "Sonudih", "Sultanpur", "Sultanpur Mazra", "Tiruvottiyūr", "Vadodara", "Vasai", "Vijayavada", "Vishakhapatnam", "Yelahanka", "Zeyadah Kot")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // weatherDao = WeatherDatabase.getInstance(this@WeatherActivity).weatherDao()
        val cityDialog = Dialog(this)
        cityDialog.setContentView(R.layout.cities_dialog)
        val etCities = cityDialog.findViewById<Spinner>(R.id.citySpinnerDialog)
        val btnOkCity = cityDialog.findViewById<Button>(R.id.btnOkCityDialog)


        MobileAds.initialize(this) {}
        setAds()

        val adapter = ArrayAdapter(
            this@WeatherActivity,
            android.R.layout.simple_spinner_item,
            citiesList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        etCities.adapter = adapter

        binding.txtCityName.setOnClickListener {
            cityDialog.show()
        }
        btnOkCity.setOnClickListener {
            etCities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    selectedCityName = citiesList[position]
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
            binding.weatherProgressBar.isVisible = true
            getWeatherData(selectedCityName)
            cityDialog.dismiss()
        }
        binding.weatherProgressBar.isVisible = true
        binding.btnBackWheather.setOnClickListener {
            onBackPressed()
        }

        viewModel.weatherVM.observe(this) { weatherModel ->
            if (weatherModel != null) {
                binding.weatherProgressBar.isVisible = false
                setBgAccordingtoWeather(weatherModel.weather[0].main)
                binding.txtCityName.text = weatherModel.name
                binding.txtWheatherDate.text = getCurrentDateInFormat()
                binding.txtWheatherDay.text = getCurrentDayName()
                binding.txtHumidity.text = weatherModel.main.humidity.toString() + "%"
                binding.txtWheatherName.text = weatherModel.weather[0].main
                binding.txtWindSpeed.text = weatherModel.wind.speed.toString() + "m/s"
                binding.txtConsditions.text = weatherModel.weather.firstOrNull()?.main?: "unknown"
                binding.txtSunrise.text = convertUnixTimestampToTime(weatherModel.sys.sunrise.toLong())
                binding.txtSunset.text = convertUnixTimestampToTime(weatherModel.sys.sunset.toLong())
                binding.txtSea.text = weatherModel.main.sea_level.toString() + " hPa"
                val celsiusCharSequence: CharSequence =
                    convertRawTempToActualTemp(weatherModel.main.temp)
                binding.txtTemprature.text = celsiusCharSequence

                Log.d(
                    "WeatherActivity",
                    "\n\n\n\n...\n\n\n\n\n....n\n\nWind Speed : " + weatherModel.wind.speed.toString()
                )
            } else {
                Toast.makeText(this@WeatherActivity, "Something went wrong !!!", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        getWeatherData("")
    }

    private fun setAds() {
        mAdView = findViewById(R.id.adView)
        viewModel.showBannerAds(this,mAdView)
    }


    private fun setBgAccordingtoWeather(data: String) {
        val main = data.lowercase()
        when (main) {
            "rain" -> {
                binding.wheatherAnimation.setAnimation(R.raw.rain_animation)
                binding.weatherActivity.setBackgroundResource(R.drawable.rain_background)
                binding.wheatherAnimation.playAnimation()
                ifNight()
            }
            "sunny" -> {
                binding.wheatherAnimation.setAnimation(R.raw.sun)
                binding.weatherActivity.setBackgroundResource(R.drawable.sun_sky)
                binding.wheatherAnimation.playAnimation()
                ifNight()

            }
            "clouds" -> {
                binding.wheatherAnimation.setAnimation(R.raw.cloud_animaton)
                binding.weatherActivity.setBackgroundResource(R.drawable.cloudy_sky)
                binding.wheatherAnimation.playAnimation()
                ifNight()

            }
            "snow" -> {
                binding.wheatherAnimation.setAnimation(R.raw.snow)
                binding.weatherActivity.setBackgroundResource(R.drawable.snow)
                binding.wheatherAnimation.playAnimation()
                ifNight()
            }
            else -> {
                binding.wheatherAnimation.setAnimation(R.raw.sun)
                binding.weatherActivity.setBackgroundResource(R.drawable.sun_sky)
                binding.wheatherAnimation.playAnimation()
                ifNight()
            }
        }
    }

    private fun ifNight() {
        if(isDaytimeNow()){
            binding.btnBackWheather.setBackgroundResource(R.drawable.night_sky)
        }
    }


    fun convertUnixTimestampToTime(timestamp: Long): String {
        val date = Date(timestamp * 1000) // Convert seconds to milliseconds
        val sdf = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        return sdf.format(date)
    }
    private fun getCurrentDateInFormat(): String {
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private fun getCurrentDayName(): String {
        val currentDate = LocalDate.now()
        val dayOfWeek =
            currentDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
        return dayOfWeek
    }

    private fun convertRawTempToActualTemp(kelvinTemperature: Double): String {
        val temp = kelvinTemperature - 273.15
        return temp.toInt().toString()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this@WeatherActivity, MainActivity::class.java))
    }

    fun isDaytimeNow(): Boolean {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        return currentHour in 6..18 // Assuming daytime is between 6 AM and 6 PM
    }
    private fun getWeatherData(city: String) {
        val selectedCity = if (city.isEmpty()) "Bharuch" else city
        lifecycleScope.launch {
          viewModel.fetchWeather(selectedCity)
        }
    }
}
