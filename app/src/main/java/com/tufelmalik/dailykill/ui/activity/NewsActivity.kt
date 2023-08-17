package com.tufelmalik.dailykill.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.ActivityNewsBinding
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date

class NewsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityNewsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        val newsKey = intent.getStringExtra("key")


        val apiService = ApiInstance.apiInterface
        val newsRepository = NewsRepository(apiService)
        val viewModel: NewsViewModel by viewModels {
            NewsViewModelFactory(newsRepository)
        }

        binding.btnBackNewsActivtiy.setOnClickListener {
            onBackPressed()
        }

        viewModel.indiaNews.observe(this){newsModel->
            val articleList = newsModel?.articles ?: emptyList()
            val list = ArrayList<Article>()
            for(i in articleList){
                if(i.urlToImage == newsKey){
                    binding.apply {
                        Glide.with(this@NewsActivity)
                            .load(i.urlToImage)
                            .thumbnail(Glide.with(this@NewsActivity).load(R.drawable.loading))
                            .into(binding.imgNewsNewsActivity)
                        binding.txtTitleNewsActivity.text = i.title
                        binding.txtPublishedAtNewsActivity.text = i.publishedAt
                        binding.txtDescriptionNewsActivity.text = i.description
                        settingPublishedTime(i.publishedAt)
                    }
                }
            }
        }



    }
    private fun settingPublishedTime(publishedAt: String) {
        val timestamp = publishedAt
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(timestamp)
        val formattedDate: String = outputFormat.format(date)
        binding.txtPublishedAtNewsActivity.text = formattedDate
    }
}