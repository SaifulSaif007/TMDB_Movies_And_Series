package com.saiful.tmdbmoviesseries

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TMDBApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}