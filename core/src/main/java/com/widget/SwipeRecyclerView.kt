package com.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.core.BaseCustomLayout
import com.core.R

class SwipeRecyclerView(context: Context, attributeSet: AttributeSet) :
    BaseCustomLayout(context, attributeSet) {

    var onCustomSwipeListener: OnCustomSwipeListener? = null
    var onLoadMoreListener: OnLoadMoreListener? = null

    lateinit var rcvCore: RecyclerView
    lateinit var swipeToRefresh: SwipeRefreshLayout

    override fun getLayoutId(): Int = R.layout.swipe_recycler_view

    override fun updateUI() {
        rcvCore = findViewById(R.id.rcv)
        swipeToRefresh = findViewById(R.id.swipeToRefresh)
        swipeToRefresh.setOnRefreshListener { onCustomSwipeListener?.onRefresh() }

        rcvCore.addOnScrollListener(object : AppScrollListener() {
            override fun onLoadMore() {
                onLoadMoreListener?.onLoadMore()
            }
        })
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(adapter: RecyclerView.Adapter<VH>) {
        rcvCore.setHasFixedSize(true)
        rcvCore.layoutManager = LinearLayoutManager(context)
        rcvCore.adapter = adapter
    }

    fun <VH : RecyclerView.ViewHolder> setUpGrid(
        adapter: RecyclerView.Adapter<VH>,
        spanCount: Int
    ) {
        rcvCore.setHasFixedSize(true)
        rcvCore.layoutManager = GridLayoutManager(context, spanCount)
        rcvCore.adapter = adapter
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        adapter: RecyclerView.Adapter<VH>,
        isHasFixedSize: Boolean,
        isNestedScrollingEnabled: Boolean
    ) {
        rcvCore.setHasFixedSize(isHasFixedSize)
        rcvCore.layoutManager = LinearLayoutManager(context)
        rcvCore.adapter = adapter
        rcvCore.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        adapter: RecyclerView.Adapter<VH>,
        isNestedScrollingEnabled: Boolean
    ) {
        rcvCore.setHasFixedSize(true)
        rcvCore.layoutManager = LinearLayoutManager(context)
        rcvCore.adapter = adapter
        rcvCore.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    fun enableRefresh() {
        swipeToRefresh.isRefreshing = true
    }

    fun cancelRefresh() {
        swipeToRefresh.isRefreshing = false
    }

    fun isRefresh() = swipeToRefresh.isRefreshing

    fun interface OnCustomSwipeListener {
        fun onRefresh()
    }

    fun interface OnLoadMoreListener {
        fun onLoadMore()
    }
}