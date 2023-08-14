package com.tufelmalik.dailykill.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.common.Constants
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.databinding.ActivityMainBinding
import com.tufelmalik.dailykill.ui.fragments.NewsFragment
import com.tufelmalik.dailykill.ui.fragments.SavedNewsFragment
import com.tufelmalik.dailykill.ui.fragments.WeatherFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var repository = NewsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        try {
            CoroutineScope(Dispatchers.IO).launch {
                val newsList = repository.getAllNews()
                //Toast.makeText(this@MainActivity,"${newsList.size.toString()}",Toast.LENGTH_SHORT).show()
                Log.d("malik", "Response--------------->: ${newsList}")
            }
        } catch (e: Exception) {
            Log.e("JSONParsingError", "Error decoding JSON: ${e.message}")
            // Handle the JSON parsing error
        }








        binding.bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.idHome_nav -> {
                    replaceFragment(NewsFragment())

                    binding.mainHeader.text = "DailyHunt"
                }
                R.id.idFav_nav -> {
                    binding.mainHeader.text = "Saved News"
                    replaceFragment(SavedNewsFragment())
                }
                R.id.idWether_nav -> {
                    binding.mainHeader.text = "Weather Updates"
                    replaceFragment(WeatherFragment())
                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
