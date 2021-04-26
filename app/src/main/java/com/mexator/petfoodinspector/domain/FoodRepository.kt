package com.mexator.petfoodinspector.domain

import com.mexator.petfoodinspector.domain.data.FoodDetail
import com.mexator.petfoodinspector.domain.data.FoodItem
import io.reactivex.rxjava3.core.Single

typealias FoodID = Int

/**
 * Abstract data source, capable of returning list of food items
 */
interface FoodRepository {

    fun getFoodList(): Single<List<FoodItem>>

    fun getDetail(id: FoodID): Single<FoodDetail>
}