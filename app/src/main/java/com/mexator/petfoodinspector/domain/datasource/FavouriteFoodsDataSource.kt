package com.mexator.petfoodinspector.domain.datasource

import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Abstract data source, capable of returning favourite foods of user, given its ID
 */
interface FavouriteFoodsDataSource {
    /**
     * Get favorite food items
     */
    fun getFavoriteFoods(user: User): Single<List<FoodID>>

    /**
     * Add food item to favorite
     */
    fun addToFavorite(user: User, foodId: FoodID): Completable
    fun removeFromFavorite(user: User, foodId: FoodID): Completable
}