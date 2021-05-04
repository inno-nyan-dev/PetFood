package com.mexator.petfoodinspector.data.network

import android.app.Application
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mexator.petfoodinspector.AppController
import com.mexator.petfoodinspector.BuildConfig
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.User
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Inject

class RemoteFavoritesDataSource() : FavouriteFoodsDataSource {
    companion object {
        private const val baseUrl = BuildConfig.API_URL
    }
    private val petFoodAPI: PetFoodAPI

    @Inject
    lateinit var client: OkHttpClient

    init {
        AppController.component.inject(this)
    }

    init {
        val contentType: MediaType = "application/json".toMediaType()
        val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .build()
        petFoodAPI = retrofit.create(PetFoodAPI::class.java)
    }

    override fun getFavoriteFoods(user: User): Single<List<FoodID>> =
        petFoodAPI.getFavorites(user.token)
            .map { list -> list.map { it.id } }
            .onErrorReturn { emptyList() }

    override fun addToFavorite(user: User, foodId: FoodID): Completable =
        petFoodAPI.toggleFavorite(foodId, user.token)

    override fun removeFromFavorite(user: User, foodId: FoodID): Completable =
        petFoodAPI.toggleFavorite(foodId, user.token)
}