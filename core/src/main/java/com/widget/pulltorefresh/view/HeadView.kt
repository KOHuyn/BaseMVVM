package com.widget.pulltorefresh.view

import android.view.View

/**
 *Created by KO Huyn on 5/9/2021
 */
interface HeadView {
    fun begin()
    fun progress(progress: Float, all: Float)
    fun finishing(progress: Float, all: Float)
    fun loading()
    fun normal()
    fun getView(): View
}