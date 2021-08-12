package com.utils.ext

import androidx.recyclerview.widget.RecyclerView
import com.widget.AppScrollListener

/**
 * Created by KO Huyn on 12/08/2021.
 */

fun RecyclerView.setOnLoadMoreListener(onLoadMore: () -> Unit) {
    addOnScrollListener(object : AppScrollListener() {
        override fun onLoadMore() {
            onLoadMore()
        }
    })
}