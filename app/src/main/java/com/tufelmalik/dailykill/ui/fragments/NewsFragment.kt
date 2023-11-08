package com.tufelmalik.dailykill.ui.fragments

import android.content.Context
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
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.data.utilities.ApiService
import com.tufelmalik.dailykill.databinding.FragmentNewsBinding
import com.tufelmalik.dailykill.ui.adapter.NewsAdapter
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Locale

class NewsFragment : Fragment() {
    private val binding: FragmentNewsBinding by lazy {
        FragmentNewsBinding.inflate(layoutInflater)
    }
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var category: String
    private lateinit var newsList: List<Article>
    private lateinit var apiService: ApiService
    private lateinit var newsRepository: NewsRepository
    private lateinit var viewModel: NewsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        newsAdapter = NewsAdapter(requireContext(), emptyList(), "india")
        apiService = ApiInstance.apiInterface
        newsRepository = NewsRepository(apiService, context)
        viewModel = ViewModelProvider(this, NewsViewModelFactory(newsRepository)).get(NewsViewModel::class.java)

    }





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

          viewModel.changeTabBg(binding.rbBusinnessNf.id, binding.tabGroupNf) // by default business tab selected...
        newsList = listOf()

        isOnlineOrNot()




        /*
          Handling the background of selected and unselected radioButtons
          and also getting the api responce according to radioButton value...
         */
        binding.tabGroupNf.setOnCheckedChangeListener { _ , checkedId ->
            val selectedRadioButton = requireView().findViewById<RadioButton>(checkedId)
            category = selectedRadioButton.text.toString().lowercase()
            viewModel.changeTabBg(checkedId, binding.tabGroupNf)
            CoroutineScope(Dispatchers.IO).launch {
//                binding.shimmerLayout.stopShimmer()
                viewModel.getIndianNewsByCategory(category)

            }
        }
        setupSearch()

        return binding.root
    }



    override fun onResume() {
        super.onResume()
        isOnlineOrNot()
        setupRecyclerView()
    }


    private fun isOnlineOrNot() {
        if (Constants.isOnline(requireContext())) {
            getNewsByCategory()
            binding.notFoundAnimationNewsFrag.isVisible = false
        } else {
           Toast.makeText(requireContext(),"Offline Fragment ",Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter(requireContext(), emptyList(), "india")
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.isAutoMeasureEnabled = true // Enable auto measurement
        binding.newsRecycler.layoutManager = layoutManager
        binding.newsRecycler.adapter = newsAdapter
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
            val title = article.title?.lowercase(Locale.getDefault()) ?: ""
            val description = article.description?.lowercase(Locale.getDefault()) ?: ""
            val author = article.author?.lowercase(Locale.getDefault()) ?: ""

            title.contains(searchText) || description.contains(searchText) || author.contains(searchText)
        }
        newsAdapter.updateData(filteredNews)
    }



    private fun getNewsByCategory() {
//        binding.shimmerLayout.stopShimmer()
        viewModel.indiaNews.observe(viewLifecycleOwner) { newsModel ->
            val articleList = newsModel?.articles ?: emptyList()
            val filteredList = articleList.filter { it.urlToImage != null }
            newsList = filteredList
            newsAdapter.updateData(filteredList)
        }
    }
}
