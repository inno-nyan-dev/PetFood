package com.mexator.petfoodinspector.ui.foodlist.model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mexator.petfoodinspector.AppController
import com.mexator.petfoodinspector.domain.FoodListRepository
import com.mexator.petfoodinspector.domain.data.FoodID
import com.mexator.petfoodinspector.domain.data.FoodItem
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.zipWith
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import java.util.*
import javax.inject.Inject

/**
 * ViewModel for FoodListScreen. Determines its logic
 */
class FoodListViewModel : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var repository: FoodListRepository

    init {
        AppController.component.inject(this)
    }

    private val searchQueries: BehaviorSubject<String> = BehaviorSubject.create()
    private val _viewState: BehaviorSubject<FoodListViewState> = BehaviorSubject.create()
    val viewState: Observable<FoodListViewState> =
        Observable.combineLatest(
            _viewState,
            searchQueries,
            { state, query ->
                state.copy(displayedItems = state.displayedItems
                    .filter { satisfiesQuery(it, query) })
            })

    /**
     * Mechanism to show non-persistent error messages, e.g. errors
     */
    private val _tempEventsObservable = PublishSubject.create<TempEvent>()
    val tempEventsObservable: Observable<TempEvent> = _tempEventsObservable

    init {
        _viewState.onNext(FoodListViewState())
    }

    fun onAttachView() {
        _viewState.mapValue { it.copy(progress = true, error = null) }
        compositeDisposable += repository.getFoodList()
            .zipWith(
                repository.getFavoriteFoods()
                    .onErrorReturn { emptyList() }
            )
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onSuccess = { (foods, favFoods) ->
                    _viewState.onNext(
                        FoodListViewState(false, null, favFoods, foods)
                    )
                },
                onError = {
                    _viewState.onNext(
                        FoodListViewState(false, it.message ?: "Unknown error")
                    )
                }
            )
    }

    fun fav(foodID: FoodID) {
        _viewState.mapValue { it.copy(favoriteIDs = it.favoriteIDs + foodID) }
        compositeDisposable += repository.addToFavorite(foodID)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { error ->
                    Log.e(TAG, "error while adding reaction", error)

                    _tempEventsObservable.onNext(TempEvent.FavError())
                    _viewState.mapValue { it.copy(favoriteIDs = it.favoriteIDs - foodID) }
                }
            )
    }

    fun unFav(foodID: FoodID) {
        _viewState.mapValue { it.copy(favoriteIDs = it.favoriteIDs - foodID) }
        compositeDisposable += repository.removeFromFavorite(foodID)
            .subscribeOn(Schedulers.io())
            .subscribeBy(
                onError = { error ->
                    Log.e(TAG, "error while removing reaction", error)

                    _tempEventsObservable.onNext(TempEvent.FavError())
                    _viewState.mapValue { it.copy(favoriteIDs = it.favoriteIDs + foodID) }
                }
            )
    }

    fun submitQuery(query: String) = searchQueries.onNext(query)

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun satisfiesQuery(food: FoodItem, query: String): Boolean =
        food.name.lowercase(Locale.getDefault()).contains(query)

    private fun <T> BehaviorSubject<T>.mapValue(mapper: (T) -> T) =
        onNext(mapper(value ?: error("Subject has no value!")))

    companion object {
        private const val TAG = "FoodListViewModel"
    }
}