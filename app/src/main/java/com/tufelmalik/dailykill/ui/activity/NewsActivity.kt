package com.tufelmalik.dailykill.ui.activity

import android.os.Bundle
import android.util.Log
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
import kotlinx.coroutines.launch


class NewsActivity : AppCompatActivity()  {
    private lateinit var binding : ActivityNewsBinding
    private lateinit var selectedCategory : String


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
        val newsKey = intent.getStringExtra("key")
        val cat = intent.getStringExtra("key1")
        selectedCategory = cat!!

        binding.btnBackNewsActivtiy.setOnClickListener {
            onBackPressed()
        }

        Log.d("tufel","\n\n\n\n\n\n\n\n\n......\n\n\n\nCategoty: $cat\n\n\n\n\n\n\n\n......\n\n\n\n\n\n\n\n")
        //Toast.makeText(this@NewsActivity,"Category ${cat.toString()}",Toast.LENGTH_SHORT).show()


        viewModel.selectedCategory.observe(this) { cate ->
            selectedCategory  = cate
            Log.d("mmmmmmm","Selected category: "+selectedCategory.toString())
        }



       // Toast.makeText(this@NewsActivity,"Selected : $selectedCategory",Toast.LENGTH_SHORT).show()
        viewModel.indiaNews.observe(this) { newsModel ->
            val articleList = newsModel?.articles ?: emptyList()
            ArrayList<Article>()
            for (i in articleList) {
                if (i.url == newsKey) {
                    binding.apply {
                        Glide.with(this@NewsActivity)
                            .load(i.urlToImage)
                            .thumbnail(Glide.with(this@NewsActivity).load(R.drawable.loading))
                            .into(binding.imgNewsNewsActivity)
                        binding.txtTitleNewsActivity.text = i.title
                        binding.txtPublishedAtNewsActivity.text = i.publishedAt
                        binding.txtDescriptionNewsActivity.text = i.description
                        binding.txtPublishedAtNewsActivity.text =
                            Constants.formateDate(i.publishedAt)
                    }
                }
            }
        }
        getNewsByCategory()
    }


    private fun getNewsByCategory() {
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.fetchNewsData()
            viewModel.getIndianNewsByCategory(selectedCategory)
        }
    }




}