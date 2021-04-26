package com.mexator.petfoodinspector.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Data that backend requires to authenticate user
 */
@Serializable
data class UserAuthData(
    @SerialName("email")
    val login: String,
    @SerialName("password")
    val password: String
)