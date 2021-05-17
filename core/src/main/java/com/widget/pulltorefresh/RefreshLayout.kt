package com.widget.pulltorefresh

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ScrollView
import androidx.annotation.LayoutRes
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import com.core.R
import com.widget.AppScrollListener
import com.widget.pulltorefresh.utils.DisplayUtils
import com.widget.pulltorefresh.view.FooterView
import com.widget.pulltorefresh.view.HeadRefreshView
import com.widget.pulltorefresh.view.HeadView
import com.widget.pulltorefresh.view.LoadMoreView
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.properties.Delegates

/**
 *Created by KO Huyn on 5/9/2021
 */
class RefreshLayout : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet, 0) {
        initAttr(attributeSet)
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defValue: Int) : super(
        context,
        attributeSet, defValue
    ) {
        initAttr(attributeSet)
        init()
    }

    private fun initAttr(attr: AttributeSet) {
        val a: TypedArray =
            context.obtainStyledAttributes(attr, R.styleable.RefreshLayout)
        errorSrc = a.getResourceId(R.styleable.RefreshLayout_error_layout, errorSrc)
        loadingSrc = a.getResourceId(R.styleable.RefreshLayout_loading_layout, loadingSrc)
        emptySrc = a.getResourceId(R.styleable.RefreshLayout_empty_layout, emptySrc)
        a.recycle()
    }

    companion object {
        const val ANIM_TIME = 300L
        private var HEAD_HEIGHT = 40F
        private var FOOT_HEIGHT = 30F
    }

    private var headerView: HeadView? = null
    private var footerView: FooterView? = null
    private var childView: View? = null

    private var headHeight: Float = DisplayUtils.dpToPx(context, HEAD_HEIGHT).toFloat()
    private var headHeight2: Float = DisplayUtils.dpToPx(context, HEAD_HEIGHT * 2).toFloat()
    private var footHeight: Float = DisplayUtils.dpToPx(context, FOOT_HEIGHT).toFloat()
    private var footHeight2: Float = DisplayUtils.dpToPx(context, FOOT_HEIGHT * 2).toFloat()

    private var touchY by Delegates.notNull<Float>()
    private var currentY by Delegates.notNull<Float>()

    private var canLoadMore = true
    private var canRefresh = true
    private var isRefresh = false
    private var isLoadMore = false

    private var touchSlope: Int? = null

    var refreshListener: RefreshListener? = null

    private var loadingView: View? = null
    private var errorView: View? = null
    private var emptyView: View? = null

    @LayoutRes
    private var loadingSrc: Int = R.layout.layout_loading_dialog_default

    @LayoutRes
    private var emptySrc: Int = R.layout.layout_loading_dialog_default
        set(value) {
            field = value
            Log.e("emptySrc", "${R.layout.layout_loading_dialog_default} : $field ")
        }

    @LayoutRes
    private var errorSrc: Int = R.layout.layout_loading_dialog_default

    private fun cal() {
        touchSlope = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun init() {
        cal()
//        if (childCount != 1) {
//            throw IllegalArgumentException("Child only can be one")
//        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        childView = getChildAt(0)
        addHeaderView()
        addFooterView()
    }

    private fun addHeaderView() {
        if (headerView == null) {
            headerView = HeadRefreshView(context)
        } else {
            removeView(headerView?.getView())
        }
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        headerView?.getView()?.layoutParams = layoutParams
        if (headerView?.getView()?.parent != null) {
            (headerView?.getView()?.parent as? ViewGroup)?.removeAllViews()
        }
        addView(headerView?.getView(), 0)
    }

    private fun addFooterView() {
        if (footerView == null) {
            footerView = LoadMoreView(context)
        } else {
            removeView(footerView?.getView())
        }
        val layoutParams = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0)
        layoutParams.gravity = Gravity.BOTTOM
        footerView?.getView()?.layoutParams = layoutParams
        if (footerView?.getView()?.parent != null) {
            (footerView?.getView()?.parent as? ViewGroup)?.removeAllViews()
        }
        addView(footerView?.getView())
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        if (!canLoadMore && !canRefresh) return super.onInterceptTouchEvent(ev)
        when (ev?.action) {
            MotionEvent.ACTION_DOWN -> {
                touchY = ev.y
                currentY = touchY
            }
            MotionEvent.ACTION_MOVE -> {
                val dy = ev.y - currentY
                if (canRefresh) {
                    if (dy > (touchSlope ?: 0) && !canChildScrollUp()) {
                        headerView?.begin()
                        return true
                    }
                }
                if (canLoadMore) {
                    if (dy < -(touchSlope ?: 0) && !canChildScrollDown()) {
                        footerView?.begin()
                        return true
                    }
                }
            }
            else -> {
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (isRefresh || isLoadMore) return true
        when (event?.action) {
            MotionEvent.ACTION_MOVE -> {
                currentY = event.y
                var duration = (currentY - touchY) / 3F
                if (duration > 0 && canRefresh) {
                    duration = min(headHeight2, duration)
                    duration = max(0F, duration)
                    headerView?.getView()?.layoutParams?.height = duration.toInt()
                    childView?.translationY = duration
                    requestLayout()
                    headerView?.progress(duration, headHeight)
                } else {
                    if (canLoadMore) {
                        duration = min(footHeight2, abs(duration))
                        duration = max(0F, abs(duration))
                        footerView?.getView()?.layoutParams?.height = duration.toInt()
                        childView?.translationY = -duration
                        requestLayout()
                        footerView?.progress(duration, footHeight)
                    }
                }
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                val dy1 = (event.y - touchY) / 3
                if (dy1 > 0 && canRefresh) {
                    if (dy1 >= headHeight) {
                        createAnimatorTranslationY(
                            RefreshState.REFRESH,
                            min(dy1, headHeight2).toInt(), headHeight.toInt()
                        ) {
                            isRefresh = true
                            refreshListener?.refresh()
                            headerView?.loading()
                        }
                    } else if (dy1 > 0 && dy1 < headHeight) {
                        setFinish(dy1.toInt(), RefreshState.REFRESH)
                        headerView?.normal()
                    }
                } else {
                    if (canLoadMore) {
                        if (abs(dy1) >= footHeight) {
                            createAnimatorTranslationY(
                                RefreshState.LOAD_MORE,
                                min(abs(dy1), footHeight2).toInt(), footHeight.toInt()
                            ) {
                                isLoadMore = true
                                refreshListener?.loadMore()
                                footerView?.loading()
                            }
                        } else {
                            setFinish(abs(dy1).toInt(), RefreshState.LOAD_MORE)
                            footerView?.normal()
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun canChildScrollUp(): Boolean {
        return if (childView == null) false else childView?.canScrollVertically(-1) == true
    }

    private fun canChildScrollDown(): Boolean {
        return if (childView == null) false else childView?.canScrollVertically(1) == true
    }

    private fun setFinish(height: Int, @RefreshState.REFRESH_STATE state: Int) {
        createAnimatorTranslationY(state, height, 0) {
            if (state == RefreshState.REFRESH) {
                isRefresh = false
                headerView?.normal()
            } else {
                isLoadMore = false
                footerView?.normal()
            }
        }
    }

    private fun setFinish(@RefreshState.REFRESH_STATE state: Int) {
        if (state == RefreshState.REFRESH) {
            if (headerView != null && headerView?.getView()?.layoutParams?.height ?: 0 > 0 && isRefresh) {
                setFinish(headHeight.toInt(), state)
            }
        } else {
            if (footerView != null && footerView?.getView()?.layoutParams?.height ?: 0 > 0 && isLoadMore) {
                setFinish(footHeight.toInt(), state)
            }
        }
    }

    private fun createAnimatorTranslationY(
        @RefreshState.REFRESH_STATE state: Int,
        start: Int,
        purpose: Int,
        callback: CallBack?
    ) {
        val anim: ValueAnimator = ValueAnimator.ofInt(start, purpose)
        anim.duration = ANIM_TIME
        anim.addUpdateListener { valueAnimator ->
            val value = valueAnimator.animatedValue as Int
            if (state == RefreshState.REFRESH) {
                headerView?.getView()?.layoutParams?.height = value
                childView?.translationY = value.toFloat()
                if (purpose == 0) {//Kết thúc tải
                    headerView?.finishing(value.toFloat(), headHeight2)
                } else {
                    headerView?.progress(value.toFloat(), headHeight)
                }
            } else {
                footerView?.getView()?.layoutParams?.height = value
                childView?.translationY = (-value).toFloat()
                if (purpose == 0) {
                    footerView?.finishing(value.toFloat(), footHeight2)
                } else {
                    footerView?.progress(value.toFloat(), footHeight)
                }
            }
            if (value == purpose) {
                callback?.onSuccess()
            }
            requestLayout()
        }
        anim.start()
    }

    fun interface CallBack {
        fun onSuccess()
    }

    private fun showLoadingView(isShow: Boolean = true) {
        if (loadingView == null) {
            loadingView = LayoutInflater.from(context)
                .inflate(loadingSrc, null)
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            addView(loadingView, layoutParams)
        }
        loadingView?.visibility = if (isShow) VISIBLE else GONE

    }

    private fun showEmptyView(isShow: Boolean = true) {
        if (emptyView == null) {
            emptyView = LayoutInflater.from(context)
                .inflate(emptySrc, null)
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            addView(emptyView, layoutParams)
        }
        emptyView?.visibility = if (isShow) VISIBLE else GONE

    }

    private fun showErrorView(isShow: Boolean = true) {
        if (errorView == null) {
            errorView = LayoutInflater.from(context)
                .inflate(errorSrc, null)
            val layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            addView(errorView, layoutParams)
        }
        errorView?.visibility = if (isShow) VISIBLE else GONE

    }

    fun showView(@ViewRefreshStatus.VIEW_REFRESH_STATUS status: Int) {
        childView?.visibility = if (status == ViewRefreshStatus.CONTENT_STATUS) VISIBLE else GONE
        showEmptyView(status == ViewRefreshStatus.EMPTY_STATUS)
        showErrorView(status == ViewRefreshStatus.ERROR_STATUS)
        showLoadingView(status == ViewRefreshStatus.LOADING_STATUS)
    }

    fun getView(@ViewRefreshStatus.VIEW_REFRESH_STATUS status: Int): View? {
        return when (status) {
            ViewRefreshStatus.CONTENT_STATUS -> childView
            ViewRefreshStatus.EMPTY_STATUS -> emptyView
            ViewRefreshStatus.ERROR_STATUS -> errorView
            ViewRefreshStatus.LOADING_STATUS -> loadingView
            else -> childView
        }
    }

    fun setRefresh(isRefresh: Boolean) {
        if (isRefresh) {
            createAnimatorTranslationY(RefreshState.REFRESH, 0, headHeight.toInt()) {
                this.isRefresh = true
                refreshListener?.refresh()
                headerView?.loading()
            }
        } else {
            finishRefresh()
        }
    }

    fun setLoadMore(isLoadMore: Boolean) {
        if (isLoadMore) {
            createAnimatorTranslationY(RefreshState.LOAD_MORE, 0, footHeight.toInt()) {
                this.isLoadMore = true
                refreshListener?.loadMore()
                footerView?.loading()
            }
        } else {
            finishLoadMore()
        }
    }

    fun finishRefresh() {
        setFinish(RefreshState.REFRESH)
    }

    fun finishLoadMore() {
        setFinish(RefreshState.LOAD_MORE)
    }

    fun setCanLoadMore(canLoadMore: Boolean) {
        this.canLoadMore = canLoadMore
    }

    fun setCanRefresh(canRefresh: Boolean) {
        this.canRefresh = canRefresh
    }

    fun setHeaderView(headerView: HeadView) {
        this.headerView = headerView
        addHeaderView()
    }

    fun setFooterView(footerView: FooterView) {
        this.footerView = footerView
        addFooterView()
    }

    fun setHeadHeight(dp: Int) {
        headHeight = DisplayUtils.dpToPx(context, dp.toFloat()).toFloat()
    }

    fun setFootHeight(dp: Int) {
        footHeight = DisplayUtils.dpToPx(context, dp.toFloat()).toFloat()
    }

    fun setAllHeight(dp: Int) {
        headHeight = DisplayUtils.dpToPx(context, dp.toFloat()).toFloat()
        footHeight = DisplayUtils.dpToPx(context, dp.toFloat()).toFloat()
    }

    fun setAllHeight(refresh: Int, loadMore: Int) {
        headHeight = DisplayUtils.dpToPx(context, refresh.toFloat()).toFloat()
        footHeight = DisplayUtils.dpToPx(context, loadMore.toFloat()).toFloat()
    }


    fun setMaxHeadHeight(dp: Int) {
        if (headHeight >= DisplayUtils.dpToPx(context, dp.toFloat())) return
        headHeight2 = DisplayUtils.dpToPx(context, dp.toFloat()).toFloat()
    }

    fun setMaxFootHeight(dp: Int) {
        if (footHeight >= DisplayUtils.dpToPx(context, dp.toFloat())) return
        footHeight2 = DisplayUtils.dpToPx(context, dp.toFloat()).toFloat()
    }

    fun setAllMaxHeight(dp: Int) {
        if (headHeight >= DisplayUtils.dpToPx(context, dp.toFloat())) return
        if (footHeight >= DisplayUtils.dpToPx(context, dp.toFloat())) return
        headHeight2 = DisplayUtils.dpToPx(context, dp.toFloat()).toFloat()
        footHeight2 = DisplayUtils.dpToPx(context, dp.toFloat()).toFloat()
    }

    fun setAllMaxHeight(refresh: Int, loadMore: Int) {
        if (headHeight >= DisplayUtils.dpToPx(context, refresh.toFloat())) return
        if (footHeight >= DisplayUtils.dpToPx(context, loadMore.toFloat())) return
        headHeight2 = DisplayUtils.dpToPx(context, refresh.toFloat()).toFloat()
        footHeight2 = DisplayUtils.dpToPx(context, loadMore.toFloat()).toFloat()
    }

    fun attachLoadMoreToRecyclerView(rcv: RecyclerView) {
        rcv.addOnScrollListener((object : AppScrollListener() {
            override fun onLoadMore() {
                setLoadMore(true)
            }
        }))
    }

    fun attachLoadMoreToNestedScrollView(scrollView: NestedScrollView) {
        scrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (v.getChildAt(v.childCount - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.childCount - 1)).measuredHeight - v.measuredHeight) && scrollY > oldScrollY) {
                    setLoadMore(true)
                }
            }
        })
    }

}