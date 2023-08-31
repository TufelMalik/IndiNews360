package com.tufelmalik.dailykill.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.data.utilities.ApiService
import com.tufelmalik.dailykill.databinding.ActivityNewsBinding
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.launch


class NewsActivity : AppCompatActivity()  {
    private lateinit var binding : ActivityNewsBinding
    val apiService: ApiService = ApiInstance.apiInterface
    val newsRepository = NewsRepository(apiService)
    val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(newsRepository)
    }

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
            Log.d("NewsActivity","\n\n\n\nTufeln\n\n\\n\\n\n\n\n\nn"+newsTitle+"       $newsKey     $newsDes           $newsPublishDate\n\n\n\\\n\n\n\n\n\\n\n")
            Glide.with(this@NewsActivity)
                .load(newsKey)
                .thumbnail(Glide.with(this@NewsActivity).load(R.drawable.loading))
                .into(binding.imgNewsNewsActivity)
            binding.txtTitleNewsActivity.text = newsTitle
            binding.txtPublishedAtNewsActivity.text = newsPublishDate
            binding.txtDescriptionNewsActivity.text = newsDes
            Log.d("NewsActivity","\n\n\n\n\n\n\\n\\n\n\n\n\nn"+binding.txtTitleNewsActivity.text+"       $newsKey     $newsDes           $newsPublishDate\n\n\n\\\n\n\n\n\n\\n\n")



        }




}