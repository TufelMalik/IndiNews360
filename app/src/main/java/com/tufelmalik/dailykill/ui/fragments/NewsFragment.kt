package com.tufelmalik.dailykill.ui.fragments

import android.os.Bundle
import android.telecom.DisconnectCause.LOCAL
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.tufelmalik.dailykill.R
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
    private lateinit var category : String
    private  var categoryList = ArrayList<String>()

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
        categoryList.add("business")
        categoryList.add("entertainment")
        categoryList.add("general")
        categoryList.add("health")
        categoryList.add("science")
        categoryList.add("sports")
        categoryList.add("technology")


        //  set default value of tab -> business
        val defualtCategory = "business"
        changeTabBg(binding.rbBusinnessNf.id)

        checkUserNetworkState()
        searchNews()

        binding.tabGroupNf.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton = requireView().findViewById<RadioButton>(checkedId)
            category = selectedRadioButton.text.toString().lowercase()
            Log.d("NewsFragment", "Category Value : $category")
            changeTabBg(checkedId)
            CoroutineScope(Dispatchers.IO).launch {
                val categoryNews = viewModel.getIndianNewsByCategory(category)
                Log.d("NewsFragment", categoryNews.toString())
            }

        }
        return binding.root
    }

    lateinit var searchText : String
    private fun searchNews() {
        binding.etSearchNewsFrag.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                searchText = p0.toString().lowercase()
                updateRecyclerView()
            }

        })
    }

    private fun updateRecyclerView() {
        val news = ArrayList<Article>()
        for (i in news){
            var newTitle = i.title.lowercase(Locale.getDefault())
            var newsDes = i.description.lowercase(Locale.getDefault())
            var newsAuther = i.author.lowercase(Locale.getDefault())
            if(newTitle.contains(searchText) || newsDes.contains(searchText) || newsAuther.contains(searchText)){
                news.add(i)
            }
            if(news.isNotEmpty()){
                newsAdapter.updateData(news)
            }
        }
    }

    private fun changeTabBg(selectedCategory: Int) {
        val radioGroup = binding.tabGroupNf
        for (i in 0 until radioGroup.childCount) {
            val radioButton = radioGroup.getChildAt(i) as RadioButton
            if (radioButton.id != selectedCategory) {
                radioButton.setBackgroundResource(R.drawable.unselected_tab_bg)
            } else {
                radioButton.setBackgroundResource(R.drawable.selected_tab_bg)
            }
        }
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
