package com.mexator.petfoodinspector.di

import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.data.network.RemoteFavoritesDataSource
import com.mexator.petfoodinspector.data.network.RemoteFoodsDataSource
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun getUserDataSource(): UserDataSource = RemoteFoodsDataSource()

    @Singleton
    @Provides
    fun getFoodsDataSource(): FoodDataSource = RemoteFoodsDataSource()

    @Singleton
    @Provides
    fun getFavoritesDataSource(): FavouriteFoodsDataSource = RemoteFavoritesDataSource()
}