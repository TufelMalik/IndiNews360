package com.tufelmalik.dailykill.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.FragmentNewsBinding
import com.tufelmalik.dailykill.ui.adapter.NewsAdapter
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class NewsFragment : Fragment() {
    private lateinit var binding: FragmentNewsBinding
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var category: String
    private lateinit var newsList: List<Article>
    private val apiService = ApiInstance.apiInterface
    private val newsRepository = NewsRepository(apiService)
    private val viewModel: NewsViewModel by viewModels {
        NewsViewModelFactory(newsRepository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewsBinding.inflate(inflater, container, false)

        viewModel.changeTabBg(binding.rbBusinnessNf.id, binding.tabGroupNf) // by default business tab selected...
        newsList = listOf()

        checkUserNetworkState()
        setupRecyclerView()
        setupSearch()

        binding.tabGroupNf.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = requireView().findViewById<RadioButton>(checkedId)
            category = selectedRadioButton.text.toString().lowercase()
            viewModel.changeTabBg(checkedId, binding.tabGroupNf)
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.getIndianNewsByCategory(category)
            }
        }

        return binding.root
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(requireContext(), emptyList())
        binding.newsRecycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }

    private fun setupSearch() {
        binding.etSearchNewsFrag.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val searchText = p0.toString().lowercase()
                updateRecyclerView(searchText)
            }
        })
    }

    private fun updateRecyclerView(searchText: String) {
        val filteredNews = newsList.filter { article ->
            article.title?.lowercase(Locale.getDefault())?.contains(searchText) == true ||
                    article.description?.lowercase(Locale.getDefault())?.contains(searchText) == true ||
                    article.author?.lowercase(Locale.getDefault())?.contains(searchText) == true
        }

        newsAdapter.updateData(filteredNews)
    }

    private fun checkUserNetworkState() {
        if (Constants.isOnline(requireContext())) {
            binding.newsProgressBar.isVisible = true
            getNewsByCategory()
            binding.notFoundAnimationNewsFrag.isVisible = false
        } else {
            binding.notFoundAnimationNewsFrag.isVisible = true
            binding.newsProgressBar.isVisible = false
        }
    }

    private fun getNewsByCategory() {
        viewModel.indiaNews.observe(viewLifecycleOwner) { newsModel ->
            val articleList = newsModel?.articles ?: emptyList()
            val filteredList = articleList.filter { it.urlToImage != null }
            newsList = filteredList
            newsAdapter.updateData(filteredList)
            binding.newsProgressBar.isVisible = false // Hide ProgressBar once data is loaded
        }
    }
}
