package com.mexator.petfoodinspector.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mexator.petfoodinspector.BuildConfig
import com.mexator.petfoodinspector.data.network.PetFoodAPI
import dagger.Module
import dagger.Provides
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Named

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

    @Provides
    fun getJsonConverterFactory(): Converter.Factory {
        val contentType: MediaType = "application/json".toMediaType()
        return Json { ignoreUnknownKeys = true }.asConverterFactory(contentType)
    }


    @Provides
    @Named("FoodAPIRetrofit")
    fun getFoodAPIRetrofit(client: OkHttpClient, converterFactory: Converter.Factory): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(BuildConfig.API_URL)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
            .addConverterFactory(converterFactory)
            .build()

    @Provides
    fun getFoodAPI(@Named("FoodAPIRetrofit") retrofit: Retrofit): PetFoodAPI =
        retrofit
            .create(PetFoodAPI::class.java)

}