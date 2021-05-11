package com.mexator.petfoodinspector.data.network

import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.data.network.dto.UserAuthData
import com.mexator.petfoodinspector.domain.data.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class RemoteUserDataSource @Inject constructor(private val petFoodAPI: PetFoodAPI) :
    UserDataSource {

    private var currentUser: User? = null

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

    companion object {
        private const val TAG = "RemoteUserDataSource"
    }
}