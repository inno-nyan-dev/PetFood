package com.mexator.petfoodinspector.data.local

import android.content.Context
import com.mexator.petfoodinspector.domain.data.FoodDetail
import com.mexator.petfoodinspector.domain.FoodID
import com.mexator.petfoodinspector.domain.FoodRepository
import com.mexator.petfoodinspector.domain.data.FoodItem
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.ByteArrayOutputStream
import java.io.InputStream

object LocalRepository : FoodRepository {
    private const val DATA_FILENAME = "data.json"
    private val json = Json { ignoreUnknownKeys = true }

    private lateinit var appContext: Context
    fun provideAppContext(context: Context) {
        appContext = context
    }

    private val foodListSubject: BehaviorSubject<List<LocalFoodItem>> = BehaviorSubject.create()

    override fun getFoodList(): Single<List<FoodItem>> {
        val assets = appContext.assets
        val foodsJSON = assets.open(DATA_FILENAME).readToString()

        val items = json.decodeFromString<List<LocalFoodItem>>(foodsJSON)
        foodListSubject.onNext(items)

        return foodListSubject
            .map { list -> list.map { it.toFoodItem() } }
            .firstOrError()
    }

    override fun getDetail(id: FoodID): Single<FoodDetail> {
        return foodListSubject
            .map { list -> list.filter { item -> item.id == id }.get(0) }
            .map { item -> item.toFoodDetail() }
            .firstOrError()
    }

    private fun InputStream.readToString(): String {
        val result = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var length: Int
        while (read(buffer).also { length = it } != -1) {
            result.write(buffer, 0, length)
        }
        return result.toString("UTF-8")
    }
}