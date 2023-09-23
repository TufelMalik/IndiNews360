package com.tufelmalik.dailykill.ui.activity

import WeatherDao
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
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants.isOnline
import com.tufelmalik.dailykill.data.classes.WeatherDatabase
import com.tufelmalik.dailykill.data.repository.WeatherRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.ActivityWeatherBinding
import com.tufelmalik.dailykill.viewmodel.WeatherVMFactory
import com.tufelmalik.dailykill.viewmodel.WeatherViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    private var selectedCityName = ""
    private val apiInstance = ApiInstance.weatherApi
    private lateinit var weatherDao: WeatherDao
    private val weatherRepo: WeatherRepository by lazy {
        WeatherRepository(apiInstance)
    }
    private val viewModel: WeatherViewModel by viewModels {
        WeatherVMFactory(weatherRepo)
    }

    private val citiesList = listOf(
        "Delhi", "Mumbai", "Kolkata", "Bangalore", "Chennai", "Hyderabad", "Pune", "Ahmedabad",
        "Sūrat", "Prayagraj", "Lucknow", "Jaipur", "Cawnpore", "Mirzapur", "Nagpur", "Ghaziabad",
        "Vadodara", "Vishakhapatnam", "Indore", "Thane", "Bhopal", "Chinchvad", "Patna", "Bilaspur",
        "Ludhiana", "Agwar", "agra", "Madurai", "Jamshedpur", "Nasik", "Farīdabad", "Aurangabad",
        "Rajkot", "Meerut", "Jabalpur", "Kalamboli", "Vasai", "Najafgarh", "Varanasi", "Srīnagar",
        "Dhanbad", "Amritsar", "Alīgarh", "Guwahati", "Haora", "Ranchi", "Gwalior", "Chandīgarh",
        "Vijayavada", "Jodhpur", "Raipur", "Kota", "Kalkaji Devi", "Bhayandar", "Ambattūr",
        "Salt Lake City", "Bhatpara", "Kūkatpalli", "Darbhanga", "Dasarhalli", "Muzaffarpur",
        "Oulgaret", "New Delhi", "Tiruvottiyūr", "Puducherry", "Byatarayanpur", "Pallavaram",
        "Secunderabad", "Shimla", "Puri", "Shrīrampur", "Hugli", "Chandannagar", "Sultanpur Mazra",
        "Krishnanagar", "Barakpur", "Bhalswa Jahangirpur", "Nangloi Jat", "Yelahanka", "Titagarh",
        "Dam Dam", "Bansbaria", "Madhavaram", "Baj Baj", "Nerkunram", "Kendraparha", "Sijua",
        "Manali", "Chakapara", "Pappakurichchi", "Herohalli", "Madipakkam", "Sabalpur", "Salua",
        "Balasore", "Jalhalli", "Chinnasekkadu", "Jethuli", "Nagtala", "Bagalūr", "Pakri",
        "Hunasamaranhalli", "Hesarghatta", "Bommayapalaiyam", "Gundūr", "Punadih", "Hariladih",
        "Alawalpur", "Madnaikanhalli", "Kadiganahalli", "Mahuli", "Zeyadah Kot", "Arshakunti",
        "Hīrapur", "Mirchi", "Sonudih", "Sondekoppa", "Babura", "Madavar", "Kadabgeri", "Nanmangalam",
        "Taliganja", "Tarchha", "Belgharia", "Kammanhalli", "Sonnappanhalli", "Kedihati",
        "Doddajīvanhalli", "Simli Murarpur", "Sonawan", "Devanandapur", "Tribeni", "Huttanhalli",
        "Nathupur", "Bali", "Vajarhalli", "Saino", "Shekhpura", "Cachohalli", "Narayanpur Kola",
        "Gyan Chak", "Kasgatpur", "Kitanelli", "Harchandi", "Santoshpur", "Bendravadi", "Kodagihalli",
        "Harna Buzurg", "Mailanhalli", "Sultanpur"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
       // weatherDao = WeatherDatabase.getInstance(this@WeatherActivity).weatherDao()

        val cityDialog = Dialog(this)
        cityDialog.setContentView(R.layout.cities_dialog)
        val etCities = cityDialog.findViewById<Spinner>(R.id.citySpinnerDialog)
        val btnOkCity = cityDialog.findViewById<Button>(R.id.btnOkCityDialog)

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


    private fun setBgAccordingtoWeather(data: String) {
        val main = data.lowercase()
        when (main) {
            "rain" -> {
                binding.wheatherAnimation.setAnimation(R.raw.rain_animation)
                binding.weatherActivity.setBackgroundResource(R.drawable.rain_background)
                binding.wheatherAnimation.playAnimation()
            }
            "sunny" -> {
                binding.wheatherAnimation.setAnimation(R.raw.sun)
                binding.weatherActivity.setBackgroundResource(R.drawable.sun_sky)
                binding.wheatherAnimation.playAnimation()
            }
            "clouds" -> {
                binding.wheatherAnimation.setAnimation(R.raw.cloud_animaton)
                binding.weatherActivity.setBackgroundResource(R.drawable.cloudy_sky)
                binding.wheatherAnimation.playAnimation()
            }
            "snow" -> {
                binding.wheatherAnimation.setAnimation(R.raw.snow)
                binding.weatherActivity.setBackgroundResource(R.drawable.snow_background)
                binding.wheatherAnimation.playAnimation()
            }
            else -> {
                binding.wheatherAnimation.setAnimation(R.raw.sun)
                binding.weatherActivity.setBackgroundResource(R.drawable.sun_sky)
                binding.wheatherAnimation.playAnimation()
            }
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

    private fun getWeatherData(city: String) {
        val selectedCity = if (city.isEmpty()) "Bharuch" else city
        lifecycleScope.launch {
          viewModel.fetchWeather(selectedCity)
        }
    }
}
