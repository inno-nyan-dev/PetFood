package com.mexator.petfoodinspector

import com.mexator.petfoodinspector.data.network.RemoteFoodsDataSource
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

class BackendConnectivityTest {
    private val dataSource: FoodDataSource = RemoteFoodsDataSource

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