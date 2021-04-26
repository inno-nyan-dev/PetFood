package com.mexator.petfoodinspector.ui.foodlist

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mexator.petfoodinspector.domain.data.FoodItem
import com.mexator.petfoodinspector.data.network.RemoteRepository
import com.mexator.petfoodinspector.domain.FoodRepository
import com.mexator.petfoodinspector.ui.data.toUIDangerLevel
import com.mexator.petfoodinspector.ui.foodlist.recycler.FoodUI
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*

class FoodListViewModel : ViewModel() {
    data class FoodListViewState(
        val progress: Boolean = false,
        val displayedItems: List<FoodUI> = listOf(),
    )

    private val searchQueries: BehaviorSubject<String> = BehaviorSubject.create()
    private val foodListObservable: PublishSubject<List<FoodItem>> = PublishSubject.create()
    private val progressObservable: PublishSubject<Boolean> = PublishSubject.create()

    val viewState: Observable<FoodListViewState> =
        Observable.combineLatest(
            searchQueries,
            foodListObservable,
            progressObservable,
            { query, foodList, progress ->
                Log.d(TAG, query)
                FoodListViewState(
                    progress,
                    foodList.filter { satisfiesQuery(it, query) }
                        .map(this::mapItem))
            }
        )

    private val compositeDisposable = CompositeDisposable()
    private val repository: FoodRepository = RemoteRepository

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun submitQuery(query: String) = searchQueries.onNext(query)


    fun loadInitialContent() {
        compositeDisposable += repository.getFoodList()
            .doOnSubscribe { progressObservable.onNext(true) }
            .doOnSuccess { progressObservable.onNext(false) }
            .subscribeBy(
                onSuccess = { foodListObservable.onNext(it) },
                onError = { foodListObservable.onError(it) }
            )
    }

    private fun satisfiesQuery(food: FoodItem, query: String): Boolean =
        food.name.toLowerCase(Locale.getDefault()).contains(query)

    private fun mapItem(foodItem: FoodItem): FoodUI {
        return FoodUI(
            foodItem.name,
            foodItem.imageData,
            foodItem.dangerLevel.toUIDangerLevel(),
            foodItem.id
        )
    }

    companion object {
        private const val TAG = "FoodListViewModel"
    }
}