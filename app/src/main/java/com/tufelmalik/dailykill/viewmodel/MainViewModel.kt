package com.tufelmalik.dailykill.viewmodel

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.tufelmalik.dailykill.R
import com.tufelmalik.dailykill.data.classes.Constants

class MainViewModel: ViewModel(){
    val isOnlineUser: MutableLiveData<Boolean> = MutableLiveData()

    fun showBannerAds(context: Context, mAdView: AdView) {
        val adView = AdView(context)
        adView.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)
    }








}