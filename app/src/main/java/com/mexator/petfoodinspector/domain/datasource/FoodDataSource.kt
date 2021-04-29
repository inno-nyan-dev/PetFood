package com.mexator.petfoodinspector.domain.datasource

import com.mexator.petfoodinspector.domain.data.FoodDetail
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem
import io.reactivex.rxjava3.core.Single

/**
 * Abstract data source, capable of returning list of food items
 */
interface FoodDataSource {

    fun getFoodList(): Single<List<FoodItem>>

    fun getDetail(id: FoodID): Single<FoodDetail>
}