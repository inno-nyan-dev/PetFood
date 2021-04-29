package com.mexator.petfoodinspector.ui.auth.login

import androidx.lifecycle.ViewModel
import com.mexator.petfoodinspector.data.UserRepository
import com.mexator.petfoodinspector.data.network.RemoteDataSource
import com.mexator.petfoodinspector.data.network.dto.errorMessage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.HttpException

sealed class LoginViewState
object ProgressState : LoginViewState()
object SuccessState : LoginViewState()
class ErrorState(val message: String) : LoginViewState()

class LoginViewModel : ViewModel() {
    private val _viewState: BehaviorSubject<LoginViewState> = BehaviorSubject.create()
    val viewState: Observable<LoginViewState> = _viewState

    private val repository: UserRepository = RemoteDataSource
    private val compositeDisposable = CompositeDisposable()

    fun logIn(login: String, password: String) {
        _viewState.onNext(ProgressState)

        compositeDisposable += repository
            .login(login, password)
            .subscribeBy(
                onComplete = { _viewState.onNext(SuccessState) },
                onError = {
                    val message = if (it is HttpException) {
                        it.errorMessage().message
                    } else {
                        it.message ?: "Unknown error"
                    }
                    _viewState.onNext(ErrorState(message))
                }
            )
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}