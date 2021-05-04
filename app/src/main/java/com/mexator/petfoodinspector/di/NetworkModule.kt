package com.mexator.petfoodinspector.di

import com.mexator.petfoodinspector.BuildConfig
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

@Module
class NetworkModule {
    @Provides
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(
                    HttpLoggingInterceptor()
                        .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
                )
            }
            .build()
    }
}