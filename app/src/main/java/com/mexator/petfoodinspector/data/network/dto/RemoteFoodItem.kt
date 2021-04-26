package com.mexator.petfoodinspector.data.network.dto

import com.mexator.petfoodinspector.BuildConfig
import com.mexator.petfoodinspector.domain.data.DangerLevel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
class RemoteFoodItem(
    @SerialName("id")
    val id: Int,
    @SerialName("productName")
    val name: String,
    @SerialName("productImageUrl")
    private val imagePath: String,
    @SerialName("productDangLevel")
    val dangerLevel: DangerLevel,
    @SerialName("productDescription")
    val productDescription: String
) {
    @Transient
    val imageUrl = BuildConfig.IMAGE_API_URL + imagePath
}