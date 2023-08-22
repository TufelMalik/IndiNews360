package com.tufelmalik.dailykill.data.classes

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.TextView
import com.tufelmalik.dailykill.data.model.Article
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object
Constants {

    //    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
    const val NEWS_API_KEY = "f07962ab9a4e4883be33443776ac7e3a"

//    https://api.openweathermap.org/data/2.5/weather?q=Bharuch&appid=ea9e17e3495e70c73da359f3d882ddae


    const val WHEATHER_API_KEY = "3fabf25f0f20341ba60e8d93ed394822"
    const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"

    const val USA_KEY = "top-headlines?country=us&apiKey=f07962ab9a4e4883be33443776ac7e3a&pageSize=100"
    const val INDIA_KEY = "top-headlines?country=in&category=business&apiKey=f07962ab9a4e4883be33443776ac7e3a&pageSize=100"
    const val BASE_URL = "https://newsapi.org/v2/"
    const val NEWS_API_LINK = "https://newsapi.org/v2/top-headlines?country=us&apiKey=f07962ab9a4e4883be33443776ac7e3a"




    fun formateDate(publishedAt: String): String {
        val timestamp = publishedAt
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormat = SimpleDateFormat("yyyy-MM-dd")
        val date: Date = inputFormat.parse(timestamp)
        val formattedDate: String = outputFormat.format(date)
        return formattedDate
    }
    fun setDate2Days(published: TextView, publishedAt: String) {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

        try {
            val date: Date = inputFormat.parse(publishedAt)
            val currentDate = Calendar.getInstance()
            currentDate.add(Calendar.DAY_OF_MONTH, -1)

            val diffInMilliseconds = currentDate.timeInMillis - date.time
            val diffInDays = TimeUnit.MILLISECONDS.toDays(diffInMilliseconds)

            val textToShow = when {
                diffInDays == 0L -> "Today"
                diffInDays == 1L -> "1 day ago"
                diffInDays > 1L -> "$diffInDays days ago"
                else -> "Unknown"
            }

            published.text = textToShow
        } catch (e: ParseException) {
            e.printStackTrace()
            published.text = "Invalid Date"
        }
    }
    fun shareNews(context: Context?,article: Article){
        val shareText = "${article.title}\n${article.description}\n\nArticle Image Link://${article.urlToImage}"
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, shareText)
            putExtra(Intent.EXTRA_SUBJECT, "News Article")
        }
        val chooser = Intent.createChooser(shareIntent, "Share News Article")
        if (shareIntent.resolveActivity(context?.packageManager!!) != null) {
            context?.startActivity(chooser)
        } else {

        }
    }
    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }




}