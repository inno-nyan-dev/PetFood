package com.mexator.petfoodinspector

import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.di.NetworkModule
import com.mexator.petfoodinspector.di.RepositoryModule
import com.mexator.petfoodinspector.domain.datasource.FavouriteFoodsDataSource
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class BackendConnectivityTest {
    @Inject
    lateinit var foodDataSource: FoodDataSource

    @Inject
    lateinit var favDataSource: FavouriteFoodsDataSource

    @Inject
    lateinit var userDataSource: UserDataSource

    /**
     * User credentials for test user
     */
    private val testUserCredentials = "test" to "t"

    @Before
    fun setUp() {
        val component: TestComponent = DaggerTestComponent
            .builder()
            .networkModule(NetworkModule())
            .repositoryModule(RepositoryModule())
            .build()

        component.inject(this)
    }

    // Tests for FoodDataSource

    @Test
    fun testGetFoodList() {
        val testObserver = foodDataSource.getFoodList()
            .doOnSuccess { list -> if (list.isEmpty()) Assert.fail() }
            .test()
        testObserver.awaitDone(5, TimeUnit.SECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }

    @Test
    fun testGetFoodDetail() {
        val testObserver = foodDataSource.getFoodList()
            .flatMap { foodDataSource.getDetail(it[0].id) }
            .test()
        testObserver.awaitDone(5, TimeUnit.SECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }

    // Tests for UserDataSource

    @Test
    fun testLogin() {
        val testObserver =
            userDataSource.login(testUserCredentials.first, testUserCredentials.second)
                .andThen(Maybe.defer { userDataSource.getSelfUser() })
                .test()

        testObserver
            .awaitDone(5, TimeUnit.SECONDS)
            .assertValueCount(1)
    }

    @Test
    fun testLogout() {
        val testObserver =
            userDataSource.login(testUserCredentials.first, testUserCredentials.second)
                .andThen(Completable.defer { userDataSource.logout() })
                .andThen(Maybe.defer { userDataSource.getSelfUser() })
                .test()

        testObserver
            .awaitDone(5, TimeUnit.SECONDS)
            .assertNoValues()
    }

    // Tests for FavoritesDataSource

    @Test
    fun testGetFavFoods() {
        val testObserver =
            userDataSource.login(testUserCredentials.first, testUserCredentials.second)
                .andThen(Maybe.defer { userDataSource.getSelfUser() })
                .flatMapSingle { favDataSource.getFavoriteFoods(it) }
                .test()
        testObserver
            .awaitDone(5, TimeUnit.SECONDS)
            .assertValueCount(1)
    }
}