package com.core

import android.view.View

fun interface OnItemClick<T : Any> {
    fun onItemClickListener(item: T, position: Int)
}