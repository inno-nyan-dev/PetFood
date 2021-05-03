package com.mexator.petfoodinspector.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.data.network.RemoteFoodsDataSource
import com.mexator.petfoodinspector.domain.data.User
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject

data class DrawerState(
    val user: User? = null
)

class DrawerViewModel : ViewModel() {
    private val repository: UserDataSource = RemoteFoodsDataSource
    private val compositeDisposable = CompositeDisposable()

    private val _viewState: BehaviorSubject<DrawerState> = BehaviorSubject.create()
    val viewState: Observable<DrawerState> = _viewState

    /**
     * View should call this when it starts uses this viewModel and wants
     * it to emit update(s) for view state
     */
    fun onViewAttach() {
        compositeDisposable += repository.getSelfUser()
            .doOnError { Log.e(TAG, "Error getting user", it) }
            .onErrorComplete()
            .subscribeBy(
                onSuccess = { _viewState.onNext(DrawerState(it)) },
                onComplete = { _viewState.onNext(DrawerState(null)) }
            )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    companion object {
        private const val TAG = "DrawerViewModel"
    }
}