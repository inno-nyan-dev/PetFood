package com.mexator.petfoodinspector.ui.foodlist.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.databinding.ItemFoodBinding
import com.mexator.petfoodinspector.ui.recycler.BaseViewHolder
import com.mexator.petfoodinspector.ui.recycler.base.BaseHolderFactory

class FoodHolderFactory(private val foodClickCallback: FoodItemClickCallback) : BaseHolderFactory() {
    override fun createViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<*>? {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {

            R.layout.item_food -> FoodViewHolder(
                ItemFoodBinding.inflate(inflater, parent, false),
                foodClickCallback
            )
            else -> null
        }
    }
}