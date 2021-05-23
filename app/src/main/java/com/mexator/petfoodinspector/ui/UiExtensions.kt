package com.mexator.petfoodinspector.ui

import android.content.Context
import android.content.res.Resources
import androidx.annotation.Px
import androidx.viewbinding.ViewBinding

fun ViewBinding.getResources(): Resources = root.resources

@Px
fun Context.dpToPx(dp: Int): Float = resources.displayMetrics.density * dp