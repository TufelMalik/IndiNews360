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
import io.ktor.util.reflect.instanceOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWeatherBinding
    val apiInstance = ApiInstance.apiInterface
    val weatherRepo = WeatherRepository(apiInstance)
    val viewModel: WeatherViewModel by viewModels {
        WeatherVMFactory(weatherRepo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)





    }


}
