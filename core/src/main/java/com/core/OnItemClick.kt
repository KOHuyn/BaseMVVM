package com.core

import android.view.View

fun interface OnItemClick {
    fun onItemClickListener(view: View, position: Int)
}