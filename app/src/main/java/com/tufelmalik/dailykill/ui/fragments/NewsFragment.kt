package com.tufelmalik.dailykill.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.FragmentNewsBinding
import com.tufelmalik.dailykill.ui.adapter.NewsAdapter
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        val apiService = ApiInstance.apiInterface
        val newsRepository = NewsRepository(apiService)

        viewModel = ViewModelProvider(this, NewsViewModelFactory(newsRepository))
            .get(NewsViewModel::class.java)

        newsAdapter = NewsAdapter(requireContext(), emptyList())

        binding.newsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }

        viewModel.indiaNewsLiveData.observe(viewLifecycleOwner) { newsModel ->
            val articleList = newsModel?.articles ?: emptyList()
            Toast.makeText(requireContext(),"Size : ${articleList.size}",Toast.LENGTH_LONG).show()
            newsAdapter.updateData(articleList)
        }

        return binding.root
    }
}
