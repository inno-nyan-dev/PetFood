package com.mexator.petfoodinspector.ui.auth.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.mexator.petfoodinspector.data.UserRepository
import com.mexator.petfoodinspector.data.network.RemoteRepository
import com.mexator.petfoodinspector.data.pojo.ErrorResponse
import com.mexator.petfoodinspector.data.pojo.errorMessage
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException

sealed class LoginViewState
object ProgressState : LoginViewState()
object SuccessState : LoginViewState()
class ErrorState(val message: String) : LoginViewState()

class LoginViewModel : ViewModel() {
    private val _viewState: BehaviorSubject<LoginViewState> = BehaviorSubject.create()
    val viewState: Observable<LoginViewState> = _viewState

    private val repository: UserRepository = RemoteRepository
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

    companion object {
        private const val TAG = "LoginViewModel"
    }
}