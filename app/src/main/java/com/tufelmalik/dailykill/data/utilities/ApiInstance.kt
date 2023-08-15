package com.tufelmalik.dailykill.data.utilities

import com.tufelmalik.dailykill.data.classes.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance {

    private val retorfit by lazy{
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
    val apiInterface by lazy {
        retorfit.create(ApiService::class.java)
    }

}