package com.mexator.petfoodinspector

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.mexator.petfoodinspector.data.local.LocalDataSource

/**
 * Yes, I use Application as DI provider
 */
class AppController : Application() {
    override fun onCreate() {
        super.onCreate()
        LocalDataSource.provideAppContext(applicationContext)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}