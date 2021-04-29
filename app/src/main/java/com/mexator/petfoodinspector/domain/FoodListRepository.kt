package com.mexator.petfoodinspector.domain

import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import io.reactivex.rxjava3.core.Single

class FoodListRepository(
    private val foodDataSource: FoodDataSource,
    private val favouriteFoodsSource: FavouriteFoodsDataSource
): FavouriteFoodsDataSource by favouriteFoodsSource {
    /**
     * Get all food items
     */
    fun getFoodList(): Single<List<FoodItem>> = foodDataSource.getFoodList()
}