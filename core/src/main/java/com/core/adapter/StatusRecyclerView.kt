package com.core.adapter

import androidx.annotation.DrawableRes

/**
 * Created by KO Huyn on 05/08/2021.
 */

sealed class StatusRecyclerView : BaseItemRecyclerView {
    enum class TypeLoading { REFRESH, LOAD_MORE, LOADING }

    data class Loading(
        val isLoading: Boolean = false,
        var typeLoading: TypeLoading = TypeLoading.LOADING
    ) : StatusRecyclerView() {
        override val itemType: Int
            get() = when (typeLoading) {
                TypeLoading.LOAD_MORE -> BaseItemRecyclerView.TYPE_LOAD_MORE
                TypeLoading.LOADING -> BaseItemRecyclerView.TYPE_LOADING
                TypeLoading.REFRESH -> BaseItemRecyclerView.TYPE_REFRESH
            }
    }

    data class Error(
        val msg: String? = null,
        @DrawableRes val src: Int? = null,
        val showButton: Boolean = false,
        val textButton: String? = null,
        val resultCode: Int
    ) : StatusRecyclerView() {
        override val itemType: Int
            get() = BaseItemRecyclerView.TYPE_ERROR
    }
}