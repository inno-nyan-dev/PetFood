package com.mexator.petfoodinspector.ui.data

import androidx.annotation.ColorRes
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.domain.data.DangerLevel

enum class UIDangerLevel(
    val levelString: String,
    @ColorRes val colorRes: Int,
    @ColorRes val textColorRes: Int
) {
    Danger("Forbidden", R.color.red_400, R.color.white),
    Careful("Careful", R.color.yellow_400, R.color.black),
    Safe("Safe", R.color.green_300, R.color.black),
}

fun UIDangerLevel.toDangerLevel(): DangerLevel =
    when (this) {
        UIDangerLevel.Safe -> DangerLevel.SAFE
        UIDangerLevel.Danger -> DangerLevel.PROHIBITED
        UIDangerLevel.Careful -> DangerLevel.WITH_CARE
    }

fun DangerLevel.toUIDangerLevel(): UIDangerLevel =
    when (this) {
        DangerLevel.SAFE -> UIDangerLevel.Safe
        DangerLevel.PROHIBITED -> UIDangerLevel.Danger
        DangerLevel.WITH_CARE -> UIDangerLevel.Careful
    }