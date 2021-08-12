package com.core.adapter

/**
 * Created by KO Huyn on 05/08/2021.
 */

interface BaseItemRecyclerView {
    companion object {
        const val TYPE_LOADING = 9999
        const val TYPE_LOAD_MORE = 9998
        const val TYPE_REFRESH = 9997
        const val TYPE_ERROR = 9996
        const val TYPE_0_IN_NORMAL = 9995
    }

    val itemType: Int
}