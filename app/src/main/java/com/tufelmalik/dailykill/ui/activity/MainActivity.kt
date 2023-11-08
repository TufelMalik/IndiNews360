package com.tufelmalik.dailykill.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.databinding.ActivityMainBinding
import com.tufelmalik.dailykill.ui.fragments.NewsFragment
import com.tufelmalik.dailykill.ui.fragments.WorldNewsFragment
import com.tufelmalik.dailykill.viewmodel.MainViewModel
import nl.joery.animatedbottombar.AnimatedBottomBar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mAdView : AdView
    private lateinit var mainViewModel : MainViewModel

    //https://github.com/qamarelsafadi/CurvedBottomNavigation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        mainViewModel  = ViewModelProvider(this).get(MainViewModel::class.java)



        checkUserOnline()
        replaceFragment(NewsFragment())

        binding.mainHeader.text = getString(R.string.app_name)




    }

    private fun setAds() {
        mAdView = findViewById(R.id.adView)
        mainViewModel.showBannerAds(this,mAdView)
    }

    private fun checkUserOnline() {
        try{
            val result = Constants.isOnline(this)
            if(result){
                binding.notFoundAnimationMainActivity.isVisible = false
                Log.i("Internet","Online")
                setBottomNav()
                MobileAds.initialize(this) {}
                setAds()
            }else{
                Log.i("Internet","Offline")
                binding.adView.visibility = View.GONE
                Toast.makeText(this,"OFFline",Toast.LENGTH_SHORT).show()
            }
        }catch (err : Exception){
            Log.i("Internet",err.message.toString())
        }

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
                        binding.mainHeader.text = getString(R.string.app_name)
                    }
                    R.id.idWorld_nav -> {
                        binding.mainHeader.text = getString(R.string.world_news)
                        replaceFragment(WorldNewsFragment())
                    }
                    R.id.idWether_nav -> {
                        startActivity(Intent(this@MainActivity,WeatherActivity::class.java))
                    }
                    else-> R.id.idNews_nav
                }
                return true
            }
        })
    }


    fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}



