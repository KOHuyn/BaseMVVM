package com.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.core.R

class SwipeRecyclerView : FrameLayout {

    lateinit var recyclerView: RecyclerView
        private set
    private lateinit var constrainLayout: ConstraintLayout
    private lateinit var loadingLayout: ProgressBar
    private lateinit var noDataLayout: View
    private lateinit var textViewNoData: TextView
    private lateinit var buttonRetry: Button
    private lateinit var imgDescription: ImageView

    var swipeRefreshLayout: SwipeRefreshLayout = SwipeRefreshLayout(context)
        private set

    var onSwipeLayoutChange: OnSwipeLayoutChange? = null
    var textNoData: String? = "No Data"
        set(value) {
            field = value
            textViewNoData.text = textNoData
        }
    var textRetry: String? = "Retry"
        set(value) {
            field = value
            buttonRetry.text = textRetry
        }
    var isRetry: Boolean = false
        set(value) {
            field = value
            buttonRetry.visibility = if (isRetry) View.VISIBLE else View.GONE
        }

    @DrawableRes
    var srcImageDescription: Int? = null
        set(value) {
            field = value
            if (srcImageDescription != null && srcImageDescription != 0) {
                imgDescription.visibility = View.VISIBLE
                imgDescription.setImageDrawable(
                    ResourcesCompat.getDrawable(
                        resources,
                        srcImageDescription!!,
                        null
                    )
                )
            } else {
                imgDescription.visibility = View.GONE
            }
        }

    @DrawableRes
    var backgroundButtonRetry: Int? = null
        set(value) {
            field = value
            if (backgroundButtonRetry != null && backgroundButtonRetry != 0) {
                buttonRetry.background =
                    ResourcesCompat.getDrawable(resources, backgroundButtonRetry!!, null)
            }
        }

    @ColorRes
    var textColorNoData: Int? = null
        set(value) {
            field = value
            if (textColorNoData != null && textColorNoData != 0) {
                textViewNoData.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        textColorNoData!!,
                        null
                    )
                )
            } else {
                textViewNoData.setTextColor(Color.BLACK)
            }
        }

    @ColorRes
    var textColorRetry: Int? = null
        set(value) {
            field = value
            if (textColorRetry != null && textColorRetry != 0) {
                buttonRetry.setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        textColorRetry!!,
                        null
                    )
                )
            } else {
                buttonRetry.setTextColor(Color.WHITE)
            }
        }

    @Dimension
    var textSizeNoData: Int? = null
        set(value) {
            field = value
            if (textSizeNoData != null && textSizeNoData != 0) {
                textViewNoData.setTextSize(
                    TypedValue.COMPLEX_UNIT_SP,
                    resources.getDimension(textSizeNoData!!)
                )
            } else {
                textViewNoData.textSize = 20f
            }
        }

    constructor(context: Context) : super(context) {
        initLayout()
        updateUI()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initLayout()
        updateUI()
        initAttr(attrs)
    }

    fun setRefresh(isRefresh: Boolean) {
        swipeRefreshLayout.isRefreshing = isRefresh
    }

    fun isRefreshing() = swipeRefreshLayout.isRefreshing

    fun setLoadMoreVisibility(isVisible: Boolean) {
        loadingLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    fun setNoDataVisibility(isVisible: Boolean) {
        noDataLayout.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    interface OnSwipeLayoutChange {
        fun setOnRefresh()
        fun loadMore()
    }

    fun addNoDataLayout(@LayoutRes layout: Int, callback: (view: View) -> Unit) {
        noDataLayout = LayoutInflater.from(context).inflate(layout, this)
        this.requestLayout()
        callback(noDataLayout)
    }

    fun <T : ViewBinding> addNoDataLayout(layout: T, callback: (binding: T) -> Unit) {
        noDataLayout = layout.root
        this.requestLayout()
        callback(layout)
    }

    private fun getStyleableId(): IntArray? = R.styleable.SwipeRecyclerView

    private fun initDataFromStyleable(a: TypedArray) {
        textNoData = a.getString(R.styleable.SwipeRecyclerView_textNoData) ?: "No Data"
        textRetry = a.getString(R.styleable.SwipeRecyclerView_textRetry) ?: "Retry"
        srcImageDescription = a.getResourceId(R.styleable.SwipeRecyclerView_srcNoData, 0)
        backgroundButtonRetry = a.getResourceId(R.styleable.SwipeRecyclerView_backgroundRetry, 0)
        textColorNoData = a.getColor(R.styleable.SwipeRecyclerView_textColorNoData, 0)
        textColorRetry = a.getColor(R.styleable.SwipeRecyclerView_textColorRetry, 0)
        textSizeNoData = a.getDimensionPixelSize(R.styleable.SwipeRecyclerView_textSizeNoData, 0)
        isRetry = a.getBoolean(R.styleable.SwipeRecyclerView_isRetry, false)
    }

    private fun initAttr(attr: AttributeSet) {
        getStyleableId()?.let {
            val a = context.theme.obtainStyledAttributes(attr, getStyleableId()!!, 0, 0)
            try {
                initDataFromStyleable(a)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                a.recycle()
            }
        }
    }

    private fun updateUI() {
        noDataLayout.visibility = View.GONE
        loadingLayout.visibility = View.GONE

        swipeRefreshLayout.setOnRefreshListener {
            onSwipeLayoutChange?.setOnRefresh()
        }
        recyclerView.addOnScrollListener(object : AppScrollListener() {
            override fun onLoadMore() {
                onSwipeLayoutChange?.loadMore()
            }
        })
    }


    private fun initLayout() {
        swipeRefreshLayout.layoutParams =
            ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        constrainLayout = ConstraintLayout(context)
        val newParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            ConstraintLayout.LayoutParams.MATCH_PARENT, 0
        )
        recyclerView = RecyclerView(context).apply {
            id = generateViewId()
            overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            clipToPadding = false
        }
        constrainLayout.addView(recyclerView, newParams)

        noDataLayout = layoutNoDataRetry()
        val noDataLayoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            ConstraintLayout.LayoutParams.WRAP_CONTENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        this.noDataLayout.id = View.generateViewId()
        constrainLayout.id = View.generateViewId()

        constrainLayout.addView(this.noDataLayout, noDataLayoutParams)

        val layoutConstraint = this.noDataLayout.layoutParams as ConstraintLayout.LayoutParams
        layoutConstraint.startToStart = constrainLayout.id
        layoutConstraint.endToEnd = constrainLayout.id
        layoutConstraint.topToTop = constrainLayout.id
        layoutConstraint.bottomToBottom = constrainLayout.id

        loadingLayout = ProgressBar(context).apply {
            id = generateViewId()
            isIndeterminate = true
        }
        val loadingLayoutParams: ViewGroup.LayoutParams = ViewGroup.LayoutParams(
            100,
            100
        )
        constrainLayout.addView(loadingLayout, loadingLayoutParams)
        val constraintLoading = loadingLayout.layoutParams as ConstraintLayout.LayoutParams
        constraintLoading.startToStart = constrainLayout.id
        constraintLoading.endToEnd = constrainLayout.id
        constraintLoading.topToBottom = recyclerView.id
        constraintLoading.bottomToBottom = constrainLayout.id

        val constraintRecyclerView = recyclerView.layoutParams as ConstraintLayout.LayoutParams
        constraintRecyclerView.startToStart = constrainLayout.id
        constraintRecyclerView.endToEnd = constrainLayout.id
        constraintRecyclerView.topToTop = constrainLayout.id
        constraintRecyclerView.bottomToTop = loadingLayout.id

        swipeRefreshLayout.addView(constrainLayout, newParams)
        addView(swipeRefreshLayout)
    }

    private fun layoutNoDataRetry(): LinearLayout {
        val rootView = LinearLayout(context).apply { orientation = LinearLayout.VERTICAL }
        val wrapLayout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply { gravity = Gravity.CENTER }

        rootView.layoutParams = wrapLayout
        imgDescription = ImageView(context)
        imgDescription.apply {
            layoutParams = LinearLayout.LayoutParams(300, 300)
            scaleType = ImageView.ScaleType.FIT_CENTER
            (layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER
        }
        rootView.addView(imgDescription)
        if (srcImageDescription != null && srcImageDescription != 0) {
            imgDescription.visibility = View.VISIBLE
            imgDescription.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    srcImageDescription!!,
                    null
                )
            )
        } else {
            imgDescription.visibility = View.GONE
        }
        textViewNoData = TextView(context).apply {
            if (textSizeNoData != null && textSizeNoData != 0) {
                setTextSize(TypedValue.COMPLEX_UNIT_SP, resources.getDimension(textSizeNoData!!))
            } else {
                textSize = 20f
            }

            text = textNoData
            setPadding(0, 20, 0, 20)
            if (textColorNoData != null && textColorNoData != 0) {
                setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        textColorNoData!!,
                        null
                    )
                )
            } else {
                setTextColor(Color.BLACK)
            }
        }
        rootView.addView(textViewNoData, wrapLayout)
        buttonRetry = Button(context).apply {
            text = textRetry
            if (textColorRetry != null && textColorRetry != 0) {
                setTextColor(
                    ResourcesCompat.getColor(
                        resources,
                        textColorRetry!!,
                        null
                    )
                )
            } else {
                setTextColor(Color.WHITE)
            }
            if (backgroundButtonRetry != null && backgroundButtonRetry != 0) {
                background = ResourcesCompat.getDrawable(resources, backgroundButtonRetry!!, null)
            }
        }
        rootView.addView(buttonRetry, wrapLayout)
        buttonRetry.setOnClickListener { onSwipeLayoutChange?.setOnRefresh() }
        return rootView
    }

    abstract class AppScrollListener : RecyclerView.OnScrollListener() {

        private var lastCompletelyVisibleItemPosition: Int = 0
        private var firstVisibleItemPosition: Int = 0

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager
            val size = layoutManager!!.itemCount
            if (layoutManager is LinearLayoutManager) {
                findFirstAndLastVisible(layoutManager)
            }

            if (dy > 0 && size - lastCompletelyVisibleItemPosition <= 4) {
                onLoadMore()
            }
        }

        private fun findFirstAndLastVisible(layoutManager: LinearLayoutManager) {
            firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            lastCompletelyVisibleItemPosition =
                layoutManager.findLastCompletelyVisibleItemPosition()
        }

        abstract fun onLoadMore()
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(adapter: RecyclerView.Adapter<VH>) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
    }

    fun <VH : RecyclerView.ViewHolder> setUpGrid(
        adapter: RecyclerView.Adapter<VH>,
        spanCount: Int
    ) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(context, spanCount)
        recyclerView.adapter = adapter
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        adapter: RecyclerView.Adapter<VH>,
        isHasFixedSize: Boolean,
        isNestedScrollingEnabled: Boolean
    ) {
        recyclerView.setHasFixedSize(isHasFixedSize)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = isNestedScrollingEnabled
    }

    fun <VH : RecyclerView.ViewHolder> setUpRcv(
        adapter: RecyclerView.Adapter<VH>,
        isNestedScrollingEnabled: Boolean
    ) {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter
        recyclerView.isNestedScrollingEnabled = isNestedScrollingEnabled
    }
}