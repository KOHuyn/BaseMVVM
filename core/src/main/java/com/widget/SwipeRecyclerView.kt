package com.widget

import android.content.Context
import android.util.AttributeSet
import android.view.ViewStub
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.core.BaseCustomLayout
import com.core.R
import com.utils.ext.isVisible
import com.utils.ext.setVisibility

class SwipeRecyclerView(context: Context, attributeSet: AttributeSet) :
    BaseCustomLayout(context, attributeSet) {

    private lateinit var swipeToRefresh: SwipeRefreshLayout
    private lateinit var rcv: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var noData: ViewStub

    private var onCustomSwipeListener: OnCustomSwipeListener? = null
    private var onLoadMoreListener: OnLoadMoreListener? = null

    override fun getLayoutId(): Int = R.layout.swipe_recycler_view

    override fun updateUI() {
        swipeToRefresh = findViewById(R.id.swipeToRefresh)
        rcv = findViewById(R.id.rcv)
        progressBar = findViewById(R.id.loadMore)
        noData = findViewById(R.id.layout_no_data)
        noData.layoutResource = R.layout.layout_no_data
        swipeToRefresh.setColorSchemeResources(
            R.color.colorPrimary,
            R.color.colorPrimaryVariant,
            R.color.colorSecondary,
            R.color
                .colorSecondaryVariant
        )
        swipeToRefresh.setOnRefreshListener { onCustomSwipeListener?.onRefresh() }

        rcv.addOnScrollListener(object : AppScrollListener() {
            override fun onLoadMore() {
                onLoadMoreListener?.onLoadMore()
            }
        })
    }

    fun getRcv() = rcv

    fun getSwipeLayout() = swipeToRefresh

    fun setOnLoadMoreListener(callback: () -> Unit) {
        onLoadMoreListener = OnLoadMoreListener { callback() }
    }

    fun setOnRefreshListener(callback: () -> Unit) {
        onCustomSwipeListener = OnCustomSwipeListener { callback() }
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(adapter: RecyclerView.Adapter<VH>) {
        rcv.setHasFixedSize(true)
        rcv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rcv.adapter = adapter
    }

    fun <VH : RecyclerView.ViewHolder> setUpGrid(
        adapter: RecyclerView.Adapter<VH>,
        spanCount: Int
    ) {
        rcv.setHasFixedSize(true)
        rcv.layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, spanCount)
        rcv.adapter = adapter
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        adapter: RecyclerView.Adapter<VH>,
        isHasFixedSize: Boolean,
        isNestedScrollingEnabled: Boolean
    ) {
        rcv.setHasFixedSize(isHasFixedSize)
        rcv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rcv.adapter = adapter
        rcv.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        adapter: RecyclerView.Adapter<VH>,
        isNestedScrollingEnabled: Boolean
    ) {
        rcv.setHasFixedSize(true)
        rcv.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
        rcv.adapter = adapter
        rcv.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    var isRefreshing: Boolean
        get() = swipeToRefresh.isRefreshing
        set(value) {
            swipeToRefresh.isRefreshing = value
        }

    var isLoadMore: Boolean
        set(value) {
            progressBar.setVisibility(value)
        }
        get() = progressBar.isVisible()

    fun setShowViewNoData(isShow: Boolean) {
        noData.setVisibility(isShow)
    }

    fun setLayoutSrcNoData(@LayoutRes src: Int) {
        noData.layoutResource = src
    }

    fun updateLayout() = rcv.requestLayout()

    private fun interface OnCustomSwipeListener {
        fun onRefresh()
    }

    private fun interface OnLoadMoreListener {
        fun onLoadMore()
    }
}