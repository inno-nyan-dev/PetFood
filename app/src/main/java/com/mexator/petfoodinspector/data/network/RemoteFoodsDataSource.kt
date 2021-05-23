package com.mexator.petfoodinspector.data.network

import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.data.network.dto.RemoteFoodItem
import com.mexator.petfoodinspector.domain.data.FoodDetail
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import com.mexator.petfoodinspector.ui.data.FoodPicture
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Repository that is [FoodDataSource] and [UserDataSource]
 * and takes data from API
 */
class RemoteFoodsDataSource @Inject constructor(private val petFoodAPI: PetFoodAPI) :
    FoodDataSource {

    override fun getFoodList(): Single<List<FoodItem>> =
        petFoodAPI.getProducts().map { list -> list.map { it.toFoodItem() } }

    override fun getDetail(id: FoodID): Single<FoodDetail> =
        petFoodAPI.getProducts()
            .map { list -> list.filter { it.id == id }[0] }
            .map { item: RemoteFoodItem -> FoodDetail(item.toFoodItem(), item.productDescription) }

    private fun RemoteFoodItem.toFoodItem() =
        FoodItem(
            id,
            name,
            dangerLevel,
            FoodPicture.RemoteFoodPicture(imageUrl)
        )
}