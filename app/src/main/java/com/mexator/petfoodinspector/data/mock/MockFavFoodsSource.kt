package com.mexator.petfoodinspector.data.mock

import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.User
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import kotlin.random.Random

object MockFavFoodsSource : FavouriteFoodsDataSource {
    private val favList: MutableList<FoodID> = mutableListOf()

    override fun getFavoriteFoods(user: User): Single<List<FoodID>> {
        return Single.just(favList)
    }

    override fun addToFavorite(user: User, foodId: FoodID): Completable {
        if (Random.nextBoolean())
        return Completable.error(Throwable("Test error"))
            .delay(50, TimeUnit.MILLISECONDS, Schedulers.computation(), true)
        return Completable.fromRunnable {
            favList.add(foodId)
        }
    }

    override fun removeFromFavorite(user: User, foodId: FoodID): Completable {
        if (Random.nextBoolean()) return Completable.error(Throwable("Test error"))
        return Completable.fromRunnable {
            val newList = favList.filterNot { foodId == it }
            favList.clear()
            favList.addAll(newList)
        }
    }
}