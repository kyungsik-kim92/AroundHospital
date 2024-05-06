package com.example.aroundhospital

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.kakao.vectormap.KakaoMapSdk
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        KakaoMapSdk.init(this, "5d205f362e46b908c25920b3c0d459ed")
    }
}