package com.mexator.petfoodinspector.ui.foodlist.model

import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem

/**
 * State of food list screen.
 *
 * There should not be an error and content on screen simultaneously, i.e.
 * if [error] is not null, then [displayedItems] should be null and vice versa
 */
data class FoodListViewState(
    val progress: Boolean = false,
    val error: String? = null,
    val favoriteIDs: List<FoodID> = listOf(),
    val displayedItems: List<FoodItem> = listOf(),
)
