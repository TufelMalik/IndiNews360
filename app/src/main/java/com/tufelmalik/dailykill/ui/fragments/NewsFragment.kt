package com.tufelmalik.dailykill.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.FragmentNewsBinding
import com.tufelmalik.dailykill.ui.adapter.NewsAdapter
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    val apiService = ApiInstance.apiInterface
    val newsRepository = NewsRepository(apiService)
    val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(newsRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)




        checkUserNetworkState()


        return binding.root
    }


    private fun checkUserNetworkState() {
        if (Constants.isOnline(requireContext())) {
            binding.newsProgressBar.isVisible = true
            fetchNews()
            binding.notFoundAnimationNewsFrag.isVisible = false
        } else {
            binding.notFoundAnimationNewsFrag.isVisible = true
            binding.newsProgressBar.isVisible = false
        }
    }

    private fun fetchNews() {
        viewModel.indiaNews.observe(viewLifecycleOwner) { newsModel ->
            val articleList = newsModel?.articles ?: emptyList()
            val filteredList = articleList.filter { it.urlToImage != null }
            if (filteredList.isNotEmpty()) {
                newsAdapter = NewsAdapter(requireContext(), filteredList)
                newsAdapter.updateData(filteredList)
                binding.newsRecycler.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = newsAdapter
                }
                binding.newsProgressBar.isVisible = false // Hide ProgressBar once data is loaded
            } else {
                Toast.makeText(requireContext(), "No valid data available", Toast.LENGTH_SHORT).show()
            }
        }
    }


}
