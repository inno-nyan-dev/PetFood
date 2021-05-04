package com.mexator.petfoodinspector.data

import com.mexator.petfoodinspector.domain.data.User
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

/**
 * Abstract data source, that is aware, if a user is logged in to app,
 * provides capability to log in or register, and can retrieve user
 * profile information
 */
interface UserDataSource {
    /**
     * Returns `true`, if user is logged in into app
     * If `true` is returned, subsequent call to [getSelfUser] should
     * return a user data, if [logout] was not called
     *
     * @see getSelfUser
     */
    fun isUserLoggedIn(): Boolean

    /**
     * Function to login with user credentials. If it completes without errors,
     * log in was completed successfully.
     *
     * It also remembers user, which can be obtained then with [getSelfUser]
     *
     * @throws IllegalStateException if a user is already logged in
     * @see logout
     */
    fun login(username: String, password: String): Completable

    /**
     * Erase any user-related information from app
     *
     * @throws IllegalStateException if no user is logged in
     *
     * @see login
     * @see register
     */
    fun logout(): Completable

    /**
     * Try to create new user. If user is successfully created,
     * returned `Single` will emit a value, otherwise it will end with
     * error.
     *
     * It also remembers user, which can be obtained then with [getSelfUser]
     */
    fun register(username: String, password: String): Single<User>

    /**
     * Return current user. If no user is logged in, returned `Maybe` will
     * end without emitting anything
     *
     * @see isUserLoggedIn
     */
    fun getSelfUser(): Maybe<User>
}