package com.mexator.petfoodinspector.data.mock

import com.mexator.petfoodinspector.domain.data.FoodDetail
import com.mexator.petfoodinspector.domain.FoodID
import com.mexator.petfoodinspector.domain.FoodRepository
import com.mexator.petfoodinspector.domain.data.DangerLevel
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.ui.data.FoodPicture
import io.reactivex.rxjava3.core.Single
import java.util.concurrent.TimeUnit

object MockRepository : FoodRepository {
    private val urls = listOf<String>(
        "https://public-media.si-cdn.com/filer/d5/24/d5243019-e0fc-4b3c-8cdb-48e22f38bff2/istock-183380744.jpg",
        "https://cdn.britannica.com/75/174375-050-BC6968AE/Hazelnuts-fruit-compound-ovary-seed.jpg",
        "https://upload.wikimedia.org/wikipedia/commons/7/70/Chocolate_%28blue_background%29.jpg",
    )
    private var id: Int = 0
        get() = field++

    private val foods = listOf<FoodItem>(
        FoodItem(id, "Banana", DangerLevel.WITH_CARE, FoodPicture.RemoteFoodPicture(urls[0])),
        FoodItem(id, "Nut", DangerLevel.SAFE, FoodPicture.RemoteFoodPicture(urls[1])),
        FoodItem(id, "Chocolate", DangerLevel.PROHIBITED, FoodPicture.RemoteFoodPicture(urls[2])),
        FoodItem(
            id,
            "Item with very, very, very, very, very, very, very, very, very, very, very long name",
            DangerLevel.PROHIBITED,
            FoodPicture.RemoteFoodPicture(urls[2])
        ),
    )

    override fun getFoodList(): Single<List<FoodItem>> =
        Single.just(foods)
            .delay(500, TimeUnit.MILLISECONDS)

    override fun getDetail(id: FoodID): Single<FoodDetail> =
        Single.just(
            FoodDetail(
                foods[id % urls.size],
                """
                What is Lorem Ipsum?
                Lorem Ipsum is simply dummy text of the printing and typesetting industry. 
                Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, 
                when an unknown printer took a galley of type and scrambled it to make a type 
                specimen book. It has survived not only five centuries, but also the leap into 
                electronic typesetting, remaining essentially unchanged. It was popularised in 
                the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, 
                and more recently with desktop publishing software like Aldus PageMaker including 
                versions of Lorem Ipsum.
            """.trimIndent()
            )
        )

}