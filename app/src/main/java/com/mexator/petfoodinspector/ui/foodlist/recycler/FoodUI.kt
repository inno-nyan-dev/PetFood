package com.mexator.petfoodinspector.ui.foodlist.recycler

import android.content.res.ColorStateList
import androidx.core.content.res.ResourcesCompat
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.databinding.ItemFoodBinding
import com.mexator.petfoodinspector.ui.data.FoodPicture
import com.mexator.petfoodinspector.ui.data.FoodPictureDrawableFactory
import com.mexator.petfoodinspector.ui.data.UIDangerLevel
import com.mexator.petfoodinspector.ui.getResources
import com.mexator.petfoodinspector.ui.recycler.BaseViewHolder
import com.mexator.petfoodinspector.ui.recycler.base.ViewTyped
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers

data class FoodUI(
    val name: String,
    val pictureData: FoodPicture,
    val dangerLevel: UIDangerLevel,
    override val uid: Int,
    override val viewType: Int = R.layout.item_food
) : ViewTyped

class FoodViewHolder(
    private val binding: ItemFoodBinding,
    private val clickCallback: (FoodUI) -> Unit
) :
    BaseViewHolder<FoodUI>(binding.root) {
    override fun bind(item: FoodUI) {
        binding.root.setOnClickListener { clickCallback(item) }

        Single.defer {
            Single.just(
                FoodPictureDrawableFactory().createDrawable(
                    binding.foodPicture.context,
                    item.pictureData
                )
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { binding.foodPicture.setImageDrawable(it) }
            )

        binding.foodName.text = item.name
        binding.dangerLevel.text = item.dangerLevel.levelString
        val color =
            ResourcesCompat.getColor(binding.getResources(), item.dangerLevel.colorRes, null)
        binding.dangerLevel.backgroundTintList = ColorStateList.valueOf(color)
    }
}