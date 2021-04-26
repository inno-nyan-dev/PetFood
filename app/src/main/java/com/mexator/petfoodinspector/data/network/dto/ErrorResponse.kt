package com.mexator.petfoodinspector.data.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import retrofit2.HttpException

@Serializable
class ErrorResponse(
    @SerialName("message")
    val message: String
)

fun HttpException.errorMessage() =
    response()?.errorBody()?.string()?.let {
        Json.decodeFromString<ErrorResponse>(it)
    } ?: ErrorResponse("Http ${code()}: Unknown error")
