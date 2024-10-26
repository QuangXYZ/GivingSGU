package com.sgu.givingsgu.utils

import android.app.Application

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        DataLocalManager.init(this)
    }
}
