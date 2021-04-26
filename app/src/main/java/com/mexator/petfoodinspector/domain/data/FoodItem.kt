package com.mexator.petfoodinspector.domain.data

import com.mexator.petfoodinspector.ui.data.FoodPicture

data class FoodItem(
    val id: Int,
    val name: String,
    val dangerLevel: DangerLevel,
    val imageData: FoodPicture
)