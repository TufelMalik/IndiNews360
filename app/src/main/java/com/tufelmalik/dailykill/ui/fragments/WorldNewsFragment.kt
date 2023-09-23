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
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.FragmentWorldNewsBinding
import com.tufelmalik.dailykill.ui.adapter.NewsAdapter
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory


class WorldNewsFragment : Fragment() {
    private lateinit var binding : FragmentWorldNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    val apiService = ApiInstance.apiInterface
//    private val newDao = NewsDatabase.getInstance(requireContext()).newsDao()
    val newsRepository = NewsRepository(apiService)
    val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(newsRepository)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentWorldNewsBinding.inflate(layoutInflater)
        val apiService = ApiInstance.apiInterface
        NewsRepository(apiService)


        isOnlineOrNot()

        return binding.root
    }


    private fun isOnlineOrNot() {
        val state = viewModel.checkUserNetworkState(requireContext())
        if (state.first) {
            binding.newsProgressBarFavFrag.isVisible = true
            fetchNews()
            binding.notFoundAnimationFavFrag.isVisible = false
        } else {
            binding.notFoundAnimationFavFrag.isVisible = true
            binding.newsProgressBarFavFrag.isVisible = false
        }
    }
    private fun fetchNews() {
        viewModel.usaNews.observe(viewLifecycleOwner) { newsModel ->
            val articleList = newsModel?.articles ?: emptyList()
            val filteredList = articleList.filter { it.urlToImage != null }
            if (filteredList.isNotEmpty()) {
                newsAdapter = NewsAdapter(requireContext(), filteredList,"world")
                newsAdapter.updateData(filteredList)
                binding.savedNewsRecyclerView.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    adapter = newsAdapter
                }
                binding.newsProgressBarFavFrag.isVisible = false // Hide ProgressBar once data is loaded
            } else {
                Toast.makeText(requireContext(), "No valid data available", Toast.LENGTH_SHORT).show()
            }
        }
    }

}