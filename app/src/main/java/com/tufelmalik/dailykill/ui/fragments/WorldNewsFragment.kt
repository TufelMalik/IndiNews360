package com.tufelmalik.dailykill.ui.fragments


import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.data.utilities.ApiService
import com.tufelmalik.dailykill.databinding.FragmentWorldNewsBinding
import com.tufelmalik.dailykill.ui.adapter.NewsAdapter
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory
import java.util.Locale


class WorldNewsFragment : Fragment() {
    private lateinit var binding : FragmentWorldNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsList: List<Article>
    private lateinit var apiService: ApiService
    private lateinit var newsRepository: NewsRepository
    private lateinit var viewModel: NewsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        apiService = ApiInstance.apiInterface
        newsRepository = NewsRepository(apiService, context)
        newsAdapter = NewsAdapter(requireContext(), emptyList(),"world")
    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       binding = FragmentWorldNewsBinding.inflate(layoutInflater)
        newsList = listOf()
        viewModel = ViewModelProvider(this, NewsViewModelFactory(newsRepository)).get(NewsViewModel::class.java)




        return binding.root
    }

    override fun onResume() {
        super.onResume()
        isOnlineOrNot()
    }


    private fun isOnlineOrNot() {
        val result = viewModel.checkUserNetworkState(requireContext())
        if (result) {
            setupRecyclerView()
            fetchNews()
            binding.notFoundAnimationFavFrag.isVisible = false
        } else {
            binding.notFoundAnimationFavFrag.isVisible = true
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(requireContext(), emptyList(), "world")
        binding.savedNewsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
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
            } else {
                Toast.makeText(requireContext(), "No valid data available", Toast.LENGTH_SHORT).show()
            }
        }
    }

}