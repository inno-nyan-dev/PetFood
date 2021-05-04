package com.mexator.petfoodinspector.data.network

import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.User
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RemoteFavoritesDataSource @Inject constructor(private val petFoodAPI: PetFoodAPI) :
    FavouriteFoodsDataSource {

    override fun getFavoriteFoods(user: User): Single<List<FoodID>> =
        petFoodAPI.getFavorites(user.token)
            .map { list -> list.map { it.id } }
            .onErrorReturn { emptyList() }

    override fun addToFavorite(user: User, foodId: FoodID): Completable =
        petFoodAPI.toggleFavorite(foodId, user.token)

    override fun removeFromFavorite(user: User, foodId: FoodID): Completable =
        petFoodAPI.toggleFavorite(foodId, user.token)
}