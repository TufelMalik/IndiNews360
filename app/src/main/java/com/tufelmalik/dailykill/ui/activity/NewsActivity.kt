package com.tufelmalik.dailykill.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants.setDate2Days
import com.tufelmalik.dailykill.databinding.ActivityNewsBinding


class NewsActivity : AppCompatActivity()  {
    private lateinit var binding : ActivityNewsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.btnBackNewsActivtiy.setOnClickListener {
            onBackPressed()
        }

        getAllIndiaNews()
    }


        private fun getAllIndiaNews() {
            val category = intent.getStringExtra("category")
            if(category.equals("world")){
                binding.txtHeader.text = getString(R.string.world_news)
            }
            val newsKey = intent.getStringExtra("img")
            val newsTitle = intent.getStringExtra("title")
            val newsDes = intent.getStringExtra("des")
            val newsPublishDate = intent.getStringExtra("date")

            Glide.with(this@NewsActivity)
                .load(newsKey)
                .thumbnail(Glide.with(this@NewsActivity).load(R.drawable.loading))
                .into(binding.imgNewsNewsActivity)
            binding.txtTitleNewsActivity.text = newsTitle
            setDate2Days(binding.txtPublishedAtNewsActivity,newsPublishDate!!)
            binding.txtDescriptionNewsActivity.text = newsDes
        }
}