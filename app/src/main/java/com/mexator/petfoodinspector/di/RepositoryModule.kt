package com.mexator.petfoodinspector.di

import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.data.network.PetFoodAPI
import com.mexator.petfoodinspector.data.network.RemoteFavoritesDataSource
import com.mexator.petfoodinspector.data.network.RemoteFoodsDataSource
import com.mexator.petfoodinspector.data.network.RemoteUserDataSource
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun getUserDataSource(petFoodAPI: PetFoodAPI): UserDataSource =
        RemoteUserDataSource(petFoodAPI)

    @Singleton
    @Provides
    fun getFoodsDataSource(petFoodAPI: PetFoodAPI): FoodDataSource =
        RemoteFoodsDataSource(petFoodAPI)

    @Singleton
    @Provides
    fun getFavoritesDataSource(petFoodAPI: PetFoodAPI): FavouriteFoodsDataSource =
        RemoteFavoritesDataSource(petFoodAPI)
}