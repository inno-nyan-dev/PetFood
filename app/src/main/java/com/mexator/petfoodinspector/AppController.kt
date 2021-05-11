package com.mexator.petfoodinspector

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.mexator.petfoodinspector.data.local.LocalDataSource
import com.mexator.petfoodinspector.di.AppComponent
import com.mexator.petfoodinspector.di.DaggerAppComponent
import com.mexator.petfoodinspector.di.NetworkModule

/**
 * Application's entry point. All it does - initializes Dagger
 */
class AppController : Application() {
    companion object {
        lateinit var component: AppComponent
            private set
    }

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        component = buildComponent()
    }

    private fun buildComponent() =
        DaggerAppComponent.builder()
            .networkModule(NetworkModule())
            .build()

}