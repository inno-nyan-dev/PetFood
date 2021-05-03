package com.mexator.petfoodinspector.ui.foodlist.model

sealed class TempEvent {
    class FavError(val message: String = "Log in to add/remove favorite foods"): TempEvent()
}
