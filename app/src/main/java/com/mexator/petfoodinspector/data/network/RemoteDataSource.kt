package com.mexator.petfoodinspector.data.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.mexator.petfoodinspector.BuildConfig
import com.mexator.petfoodinspector.data.UserRepository
import com.mexator.petfoodinspector.data.network.dto.RemoteFoodItem
import com.mexator.petfoodinspector.data.network.dto.UserAuthData
import com.mexator.petfoodinspector.domain.data.FoodDetail
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.domain.data.User
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import com.mexator.petfoodinspector.ui.data.FoodPicture
import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

/**
 * Repository that is [FoodDataSource] and [UserRepository]
 * and takes data from API
 */
object RemoteDataSource : FoodDataSource, UserRepository {
    private const val baseUrl = BuildConfig.API_URL
    private val petFoodAPI: PetFoodAPI

    private var currentUser: User? = null

    init {
        val client = OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) addInterceptor(
                    HttpLoggingInterceptor()
                        .apply { setLevel(HttpLoggingInterceptor.Level.BODY) }
                )
            }
            .build()

        val contentType: MediaType = "application/json".toMediaType()
        val retrofit: Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addCallAdapterFactory(RxJava3CallAdapterFactory.createAsync())
            .addConverterFactory(Json { ignoreUnknownKeys = true }.asConverterFactory(contentType))
            .build()
        petFoodAPI = retrofit.create(PetFoodAPI::class.java)
    }

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