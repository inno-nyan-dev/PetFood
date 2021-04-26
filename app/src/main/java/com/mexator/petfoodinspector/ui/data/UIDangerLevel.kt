package com.mexator.petfoodinspector.ui.data

import androidx.annotation.ColorRes
import com.mexator.petfoodinspector.R
import com.mexator.petfoodinspector.domain.data.DangerLevel

enum class UIDangerLevel(val levelString: String, @ColorRes val colorRes: Int) {
    Danger("Danger", R.color.red_500),
    Treat("Treat", R.color.yellow_500),
    Safe("Safe", R.color.green_500),
}

fun DangerLevel.toUIDangerLevel(): UIDangerLevel =
    when (this) {
        DangerLevel.SAFE -> UIDangerLevel.Safe
        DangerLevel.PROHIBITED -> UIDangerLevel.Danger
        DangerLevel.WITH_CARE -> UIDangerLevel.Treat
    }