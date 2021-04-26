package com.mexator.petfoodinspector.data.local

import com.mexator.petfoodinspector.domain.data.FoodDetail
import com.mexator.petfoodinspector.domain.data.DangerLevel
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.ui.data.FoodPicture
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class LocalFoodItem(
    @SerialName("id")
    val id: Int,
    @SerialName("title")
    val name: String,
    @SerialName("category")
    private val dangerLevel: DangerLevel,
    @SerialName("details")
    private val description: String
) {
    @Transient
    private val imageUrl: String = "$id.jpg"

    fun toFoodItem(): FoodItem {
        return FoodItem(id, name, dangerLevel, FoodPicture.LocalFoodPicture(imageUrl))
    }

    fun toFoodDetail(): FoodDetail {
        return FoodDetail(
            toFoodItem(),
            description
        )
    }
}
