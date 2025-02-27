package com.saiful.tmdb2

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TMDBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}