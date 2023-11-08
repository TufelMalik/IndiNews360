package com.tufelmalik.dailykill.ui.activity

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.gms.ads.AdView
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants
import com.tufelmalik.dailykill.data.model.Article
import com.tufelmalik.dailykill.data.model.Source
import com.tufelmalik.dailykill.data.repository.NewsRepository
import com.tufelmalik.dailykill.data.utilities.ApiInstance
import com.tufelmalik.dailykill.databinding.ActivityNewsBinding
import com.tufelmalik.dailykill.viewmodel.NewsViewModel
import com.tufelmalik.dailykill.viewmodel.NewsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var mAdView: AdView
    private var newsTitle: String? = null
    private var newImage: String? = null
    private var articalUrl: String? = null
    private var newPublishedDate: String? = null
    private var newsContent: String? = null
    private lateinit var artical: Article
    private val languages = arrayOf(
        "Arabic",
        "Chinese",
        "Czech",
        "Danish",
        "Dutch",
        "English",
        "Finnish",
        "French",
        "German",
        "Greek",
        "Hebrew",
        "Hindi",
        "Hungarian",
        "Indonesian",
        "Italian",
        "Japanese",
        "Korean",
        "Malay",
        "Norwegian",
        "Polish",
        "Portuguese",
        "Russian",
        "Spanish",
        "Swahili",
        "Swedish",
        "Tamil",
        "Thai",
        "Turkish",
        "Urdu",
        "Vietnamese"
    )

    private val apiService = ApiInstance.apiInterface
    private lateinit var newsRepository: NewsRepository
    private lateinit var newsViewModel: NewsViewModel
    val translate = TranslateOptions.getDefaultInstance().service






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        newsRepository = NewsRepository(ApiInstance.apiInterface,this)
        newsViewModel = ViewModelProvider(this, NewsViewModelFactory(newsRepository)).get(NewsViewModel::class.java)

        setBannerAds()

        binding.btnShareNews.setOnClickListener {
            Constants.shareNews(this@NewsActivity,artical)
        }

        binding.btnBrowseNews.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(articalUrl))
            startActivity(intent)
        }


        binding.btnBackNewsActivtiy.setOnClickListener {
            onBackPressed()
        }

        binding.btnTranslateNews.setOnClickListener {
//            showLanguageTranslateDialog()
            Toast.makeText(this,"Comming soon...",Toast.LENGTH_SHORT).show()

        }


        getAllIndiaNews()
    }


    override fun onStart() {
        super.onStart()
        newsRepository = NewsRepository(apiService,this)
        newsViewModel = ViewModelProvider(this, NewsViewModelFactory(newsRepository)).get(NewsViewModel::class.java)
    }

    private fun showLanguageTranslateDialog() {
        val dialogView = layoutInflater.inflate(R.layout.language_convert_dialog, null)
        val languageSpinner = dialogView.findViewById<Spinner>(R.id.languageSpinner)
        val btnLanguageTranslater = dialogView.findViewById<Button>(R.id.btnTranslate)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        languageSpinner.adapter = adapter

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

//        dialog.show()


        btnLanguageTranslater.setOnClickListener {
            val selectedLanguage = languageSpinner.selectedItem as String

            // Use Kotlin Coroutines to perform translation asynchronously
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val translation = translate.translate(
                        binding.txtDescriptionNewsActivity.text.toString(),
                        Translate.TranslateOption.targetLanguage(selectedLanguage)
                    )

                    // Update the UI on the main thread with the translated text
                    withContext(Dispatchers.Main) {
                        binding.txtDescriptionNewsActivity.text = translation.translatedText

                    }
                } catch (e: Exception) {
                    // Handle exceptions if translation fails
                    e.printStackTrace()
                }
            }
        }
    }


    private fun setBannerAds() {
        mAdView = findViewById(R.id.adView)
        newsViewModel.showBannerAds(this,mAdView)
    }



    private fun getAllIndiaNews() {
        val category = intent.getStringExtra("category")
        if(category.equals("world")){
            binding.txtHeader.text = getString(R.string.world_news)
        }
        newImage = intent.getStringExtra("img")
        newsTitle = intent.getStringExtra("title")
        newsContent = intent.getStringExtra("des")
        newPublishedDate = intent.getStringExtra("date")
        articalUrl = intent.getStringExtra("link")
        val source = Source("e","")
        artical = Article(source,newsTitle!!,newsTitle!!,newsContent!!,articalUrl!!,newImage!!,newPublishedDate!!,newsContent!!)
        Glide.with(this@NewsActivity)
            .load(newImage)
            .thumbnail(Glide.with(this@NewsActivity).load(R.drawable.loading))
            .into(binding.imgNewsNewsActivity)
        binding.txtTitleNewsActivity.text = newsTitle
        binding.txtPublishedAtNewsActivity.text = Constants.formateDate(newPublishedDate.toString())
        binding.txtDescriptionNewsActivity.text = newsContent
    }
}
