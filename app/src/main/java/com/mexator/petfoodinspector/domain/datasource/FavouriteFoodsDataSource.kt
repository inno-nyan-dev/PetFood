package com.mexator.petfoodinspector.domain.datasource

import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single

/**
 * Abstract data source, capable of returning favourite foods of user, given its ID
 */
interface FavouriteFoodsDataSource {
    /**
     * Get favorite food items
     */
    fun getFavoriteFoods(): Single<List<FoodID>>
    /**
     * Add food item to favorite
     */
    fun addToFavorite(food: FoodID):Completable
    fun removeFromFavorite(foodId:FoodID):Completable
}