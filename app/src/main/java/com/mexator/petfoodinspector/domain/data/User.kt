package com.mexator.petfoodinspector.domain.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("id")
    val id: Int,
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val passHash: String,
    @SerialName("token")
    val token: String
)