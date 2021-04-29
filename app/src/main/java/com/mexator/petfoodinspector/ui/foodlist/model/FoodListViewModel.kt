package com.mexator.petfoodinspector.ui.foodlist.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mexator.petfoodinspector.data.mock.MockFavFoodsSource
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.data.network.RemoteDataSource
import com.mexator.petfoodinspector.domain.FoodListRepository
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.datasource.FoodDataSource
import com.mexator.petfoodinspector.ui.data.toDangerLevel
import com.mexator.petfoodinspector.ui.data.toUIDangerLevel
import com.mexator.petfoodinspector.ui.foodlist.recycler.FoodUI
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.functions.BiFunction
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.zipWith
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashSet

class FoodListViewModel : ViewModel() {
    private val searchQueries: BehaviorSubject<String> = BehaviorSubject.create()
    private var foodListObservable: PublishSubject<Result<List<FoodItem>>> = PublishSubject.create()
    private var favFoodsObservable: BehaviorSubject<Result<List<FoodItem>>> =
        BehaviorSubject.create()
    private val progressObservable: PublishSubject<Boolean> = PublishSubject.create()

    val viewState: Observable<FoodListViewState> =
        Observable.combineLatest(
            searchQueries,
            favFoodsObservable,
            foodListObservable,
            progressObservable,
        )
        { query, favFoodsOrError, foodListOrError, progress ->
            Log.d(TAG, "$query, $foodListOrError, $progress")
            var error: Throwable? = null
            favFoodsOrError.onFailure { error = it }
            foodListOrError.onFailure { error = it }

            val favList: List<FoodItem> = favFoodsOrError.getOrNull()
                ?: listOf()
            val foodList: List<FoodItem> =
                (foodListOrError.getOrNull() ?: listOf())
                    .filterNot { it in favList }

            val displayList = favList.map { mapItem(it, true) }
                .plus(foodList.map { mapItem(it, false) })
                .filter { satisfiesQuery(it, query) }
                .toList()

            FoodListViewState(
                progress, error?.message, displayList
            )
        }


    private val compositeDisposable = CompositeDisposable()
    private val repository: FoodListRepository =
        FoodListRepository(RemoteDataSource, MockFavFoodsSource)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun submitQuery(query: String) = searchQueries.onNext(query)


    fun loadInitialContent() {
        progressObservable.onNext(true)
        compositeDisposable += repository.getFoodList()
            .doOnTerminate { progressObservable.onNext(false) }
            .subscribeBy(
                onSuccess = { foodListObservable.onNext(Result.success(it)) },
                onError = {
                    Log.e(TAG, "error receiving foods", it)
                    foodListObservable.onNext(Result.failure(it))
                }
            )
        compositeDisposable += repository.getFavoriteFoods()
            .doOnTerminate { progressObservable.onNext(false) }
            .subscribeBy(
                onSuccess = { favFoodsObservable.onNext(Result.success(it)) },
                onError = {
                    Log.e(TAG, "error receiving favorite foods", it)
                    foodListObservable.onNext(Result.failure(it))
                }
            )
    }

    fun addFav(foodUI: FoodUI) {
        val foodItem = FoodItem(
            foodUI.uid,
            foodUI.name,
            foodUI.dangerLevel.toDangerLevel(),
            foodUI.pictureData
        )
        repository.addToFavorite(foodItem).subscribeBy(
            onComplete = {
                favFoodsObservable.onNext(
                    favFoodsObservable.value.map {
                        it + foodItem
                    }
                )
            },
            onError = { favFoodsObservable.onNext(Result.failure(it)) }
        )
    }

    fun removeFav(foodID: FoodID) {
        repository.removeFromFavorite(foodID).subscribeBy(
            onComplete = {
                favFoodsObservable.onNext(
                    favFoodsObservable.value.map { list ->
                        list.filterNot { it.id == foodID }
                    }
                )
            },
            onError = { favFoodsObservable.onNext(Result.failure(it)) }
        )
    }

    private fun satisfiesQuery(food: FoodUI, query: String): Boolean =
        food.name.toLowerCase(Locale.getDefault()).contains(query)

    private fun mapItem(foodItem: FoodItem, isFavorite: Boolean): FoodUI {
        return FoodUI(
            foodItem.name,
            foodItem.imageData,
            foodItem.dangerLevel.toUIDangerLevel(),
            isFavorite,
            foodItem.id
        )
    }

    companion object {
        private const val TAG = "FoodListViewModel"
    }
}