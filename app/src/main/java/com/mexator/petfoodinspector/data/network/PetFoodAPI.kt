package com.mexator.petfoodinspector.data.network

import com.mexator.petfoodinspector.data.network.dto.RemoteFoodItem
import com.mexator.petfoodinspector.data.network.dto.UserAuthData
import com.mexator.petfoodinspector.domain.data.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import retrofit2.http.*

/**
 * Defines API calls that app makes to server
 */
interface PetFoodAPI {
    @POST("account/signup")
    fun signUp(@Body authData: UserAuthData): Single<User>

    @POST("account/login")
    fun logIn(@Body authData: UserAuthData): Single<User>

    @GET("products/products")
    fun getProducts(): Single<List<RemoteFoodItem>>

    @GET("favorites/favorites")
    fun getFavorites(
        @Header("access-token") token: String
    ): Single<List<RemoteFoodItem>>

    @POST("favorites/favorites")
    fun toggleFavorite(
        @Query("productId") productId: Int,
        @Header("access-token") token: String
    ): Completable
}