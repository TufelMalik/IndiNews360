package com.tufelmalik.dailykill.ui.activity

import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {

    private lateinit var binding  : ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dialog = Dialog(this@SplashActivity)
        dialog.setContentView(R.layout.offline_layout)
        val btnReload = dialog.findViewById<Button>(R.id.btnReload)
        if(Constants.isOnline(this@SplashActivity)){
            dialog.hide()
            openMainActivity()
        }else{
            dialog.show()
            btnReload.setOnClickListener {
                dialog.hide()
                if(Constants.isOnline(this@SplashActivity)){
                    dialog.hide()
                    openMainActivity()
                }else{
                    dialog.show()
                }
            }
        }


    }

    private fun openMainActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 2200)
    }


}