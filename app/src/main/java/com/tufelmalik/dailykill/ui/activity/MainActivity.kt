package com.tufelmalik.dailykill.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.databinding.ActivityMainBinding
import com.tufelmalik.dailykill.ui.fragments.NewsFragment
import com.tufelmalik.dailykill.ui.fragments.WorldNewsFragment
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    //https://github.com/qamarelsafadi/CurvedBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.mainHeader.text = getString(R.string.app_name)
        replaceFragment(NewsFragment())

        setBottomNav()



    }

    private fun setBottomNav(){
        binding.bottomNav.setOnTabInterceptListener(object : AnimatedBottomBar.OnTabInterceptListener {
            override fun onTabIntercepted(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ): Boolean {
                when (newTab.id) {
                    R.id.idNews_nav -> {
                        replaceFragment(NewsFragment())
                        binding.mainHeader.text = "DailyHunt"
                    }
                    R.id.idWorld_nav -> {
                        binding.mainHeader.text = "World News"
                        replaceFragment(WorldNewsFragment())
                    }
                    R.id.idWether_nav -> {
                        startActivity(Intent(this@MainActivity,WeatherActivity::class.java))
                    }
                    else->false
                }
                return true
            }
        })
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
