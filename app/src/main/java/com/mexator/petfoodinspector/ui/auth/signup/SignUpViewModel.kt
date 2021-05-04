package com.mexator.petfoodinspector.ui.auth.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.mexator.petfoodinspector.AppController
import com.mexator.petfoodinspector.data.UserDataSource
import com.mexator.petfoodinspector.data.network.RemoteFoodsDataSource
import com.mexator.petfoodinspector.data.network.dto.errorMessage
import com.mexator.petfoodinspector.domain.FoodListRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.plusAssign
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.subjects.BehaviorSubject
import retrofit2.HttpException
import java.util.regex.Pattern
import javax.inject.Inject

sealed class SignInViewState
object ProgressState : SignInViewState()
object SuccessState : SignInViewState()
class ErrorState(val message: String) : SignInViewState()

class SignUpViewModel : ViewModel() {
    private val _viewState: BehaviorSubject<SignInViewState> = BehaviorSubject.create()
    val viewState: Observable<SignInViewState> = _viewState

    @Inject
    lateinit var repository: UserDataSource

    init {
        AppController.component.inject(this)
    }

    private val compositeDisposable = CompositeDisposable()

    fun logIn(login: String, password: String) {
        _viewState.onNext(ProgressState)

        val isEmailValid = Patterns.EMAIL_ADDRESS.matcher(login).matches()
        val isPassValid = Pattern.compile(PASSWORD_PATTERN).matcher(password).matches()

        when {
            !isEmailValid -> _viewState.onNext(
                ErrorState(
                    "Please, enter valid email address"
                )
            )

            !isPassValid -> _viewState.onNext(
                ErrorState(
                    "Password should contain at least: " +
                            "1 number, 1 character, 1 uppercase letter, 1 special character"
                )
            )

            else -> compositeDisposable += repository
                .register(login, password)
                .ignoreElement()
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
    }

    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    companion object {
        private const val PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^*&+=])(?=\\S+$).{4,}$"
        private const val TAG = "SignUpViewModel"
    }
}