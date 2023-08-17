package com.tufelmalik.dailykill.data.classes

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Constants {


    //    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   ChatGTP APi Key
    const val NEWS_API_KEY = "f07962ab9a4e4883be33443776ac7e3a"
    const val CURRENT_WHEATHER_URL = "/current.json"
    const val WHEATHER_API_KEY = "c62eef365807446cab4175930231608"
    const val USA_KEY = "top-headlines?country=us&apiKey=f07962ab9a4e4883be33443776ac7e3a&pageSize=100"
    const val INDIA_KEY = "top-headlines?country=in&category=business&apiKey=f07962ab9a4e4883be33443776ac7e3a&pageSize=100"
    const val BASE_URL = "https://newsapi.org/v2/"
    const val API_LINK = "https://newsapi.org/v2/top-headlines?country=us&apiKey=f07962ab9a4e4883be33443776ac7e3a"


    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }


}