package com.widget.pulltorefresh.utils

import android.content.Context
import kotlin.math.roundToInt

/**
 *Created by KO Huyn on 5/9/2021
 */
object DisplayUtils {
    fun dpToPx(context: Context, dp: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dp * scale).roundToInt()
    }

    fun pxToDp(context: Context, px: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (px / scale).roundToInt()
    }

    fun spToPx(context: Context, sp: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (sp * fontScale).roundToInt()
    }

}