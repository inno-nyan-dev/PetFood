package com.mexator.petfoodinspector.data.mock

import com.mexator.petfoodinspector.domain.data.DangerLevel
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import com.mexator.petfoodinspector.ui.data.FoodPicture
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import kotlin.random.Random

object MockFavFoodsSource : FavouriteFoodsDataSource {
    private val picture =
        FoodPicture.RemoteFoodPicture("https://www.nastol.com.ua/mini/201507/142466.jpg")
    private val favList: MutableList<FoodItem> = mutableListOf(
        FoodItem(-1, "Test fav item", DangerLevel.SAFE, picture),
        FoodItem(-2, "Test fav item", DangerLevel.PROHIBITED, picture),
        FoodItem(-3, "Test fav item", DangerLevel.WITH_CARE, picture),
    )

    override fun getFavoriteFoods(): Single<List<FoodItem>> {
        return Single.just(favList)
    }

    override fun addToFavorite(food: FoodItem): Completable {
        if (Random.nextBoolean()) return Completable.error(Throwable("Test error"))
        return Completable.fromRunnable {
            favList.add(food)
        }
    }

    override fun removeFromFavorite(foodId: FoodID): Completable {
        if (Random.nextBoolean()) return Completable.error(Throwable("Test error"))
        return Completable.fromRunnable {
            favList.clear()
            favList.addAll(favList.filterNot { foodId == it.id })
        }
    }
}