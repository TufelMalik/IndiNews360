package com.tufelmalik.dailykill.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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


        viewModel.indiaNews.observe(this){newsModel->
            val articleList = newsModel?.articles ?: emptyList()
            val list = ArrayList<Article>()
            for(i in articleList){
                if(i.urlToImage == newsKey){
                    binding.apply {
                        Glide.with(this@NewsActivity).load(i.urlToImage).into(binding.imgNewsActvity)
                        binding.txtTitleNewsActvity.text = i.title
                        binding.txtPublishedDataNewsActvity.text = i.publishedAt
                        binding.txtDescriptionNewsActvity.text = i.description
                    }
                }
            }
        }

    }
}