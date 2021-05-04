package com.mexator.petfoodinspector.data.network

import com.mexator.petfoodinspector.AppController
import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.data.network.dto.RemoteFoodItem
import com.mexator.petfoodinspector.data.network.dto.UserAuthData
import com.mexator.petfoodinspector.domain.data.FoodDetail
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.domain.data.User
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import com.mexator.petfoodinspector.ui.data.FoodPicture
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

/**
 * Repository that is [FoodDataSource] and [UserDataSource]
 * and takes data from API
 */
class RemoteFoodsDataSource() : FoodDataSource,
    UserDataSource {

    @Inject
    lateinit var petFoodAPI: PetFoodAPI

    init {
        AppController.component.inject(this)
    }

    private var currentUser: User? = null

    override fun getFoodList(): Single<List<FoodItem>> =
        petFoodAPI.getProducts().map { list -> list.map { it.toFoodItem() } }

    override fun getDetail(id: FoodID): Single<FoodDetail> =
        petFoodAPI.getProducts()
            .map { list -> list.filter { it.id == id }[0] }
            .map { item: RemoteFoodItem -> FoodDetail(item.toFoodItem(), item.productDescription) }

    override fun isUserLoggedIn(): Boolean = currentUser != null

    override fun login(username: String, password: String): Completable =
        petFoodAPI.logIn(UserAuthData(username, password))
            .doOnSuccess { currentUser = it }
            .ignoreElement()

    override fun logout(): Completable {
        currentUser = null
        return Completable.complete()
    }

    override fun register(username: String, password: String): Single<User> =
        petFoodAPI.signUp(UserAuthData(username, password))
            .doOnSuccess { currentUser = it }

    override fun getSelfUser(): Maybe<User> =
        currentUser?.let { Maybe.just(it) } ?: Maybe.empty()

    private fun RemoteFoodItem.toFoodItem() =
        FoodItem(
            id,
            name,
            dangerLevel,
            FoodPicture.RemoteFoodPicture(imageUrl)
        )
}