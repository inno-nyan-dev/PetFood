package com.mexator.petfoodinspector

import com.mexator.petfoodinspector.data.network.RemoteDataSource
import com.mexator.petfoodinspector.domain.FoodDataSource
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

class BackendConnectivityTest {
    private val dataSource: FoodDataSource = RemoteDataSource

    @Test
    fun testBackendServesData() {
        val testObserver = dataSource.getFoodList()
            .doOnSuccess { list -> if (list.isEmpty()) Assert.fail() }
            .test()
        testObserver.awaitDone(5, TimeUnit.SECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }
}