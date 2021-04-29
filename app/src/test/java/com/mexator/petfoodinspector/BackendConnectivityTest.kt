package com.mexator.petfoodinspector

import com.mexator.petfoodinspector.data.network.RemoteRepository
import com.mexator.petfoodinspector.domain.FoodRepository
import org.junit.Assert
import org.junit.Test
import java.util.concurrent.TimeUnit

class BackendConnectivityTest {
    private val repository: FoodRepository = RemoteRepository

    @Test
    fun testBackendServesData() {
        val testObserver = repository.getFoodList()
            .doOnSuccess { list -> if (list.isEmpty()) Assert.fail() }
            .test()
        testObserver.awaitDone(5, TimeUnit.SECONDS)
        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)
    }
}