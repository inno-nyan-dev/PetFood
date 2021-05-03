package com.mexator.petfoodinspector.domain

import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.domain.data.User
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Notification
import io.reactivex.rxjava3.core.Single

class FoodListRepository(
    private val foodDataSource: FoodDataSource,
    private val userDataSource: UserDataSource,
    private val favouriteFoodsSource: FavouriteFoodsDataSource
) {
    class UserNotLoggedInException : Throwable("User not logged in")

    private fun getUserOrError(): Single<User> =
        userDataSource.getSelfUser()
            .toSingle()
            .materialize()
            .map {
                if (it.isOnError && it.error is NoSuchElementException) {
                    Notification.createOnError(UserNotLoggedInException())
                } else {
                    it
                }
            }
            .dematerialize { it }
            .toSingle()


    fun getFavoriteFoods(): Single<List<FoodID>> =
        getUserOrError().flatMap {
            favouriteFoodsSource.getFavoriteFoods(it)
        }

    fun addToFavorite(foodId: FoodID): Completable =
        getUserOrError().flatMapCompletable {
            favouriteFoodsSource.addToFavorite(it, foodId)
        }

    fun removeFromFavorite(foodId: FoodID): Completable =
        getUserOrError().flatMapCompletable {
            favouriteFoodsSource.removeFromFavorite(it, foodId)
        }

    /**
     * Get all food items
     */
    fun getFoodList(): Single<List<FoodItem>> = foodDataSource.getFoodList()
}