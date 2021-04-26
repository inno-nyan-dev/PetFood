package com.mexator.petfoodinspector.domain.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable()
enum class DangerLevel {
    @SerialName("safe")
    SAFE,

    @SerialName("with_care")
    WITH_CARE,

    @SerialName("prohibited")
    PROHIBITED
}