package com.widget.pulltorefresh.view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.*
import androidx.core.content.res.ResourcesCompat
import com.core.R
import com.widget.pulltorefresh.utils.DisplayUtils

/**
 *Created by KO Huyn on 5/9/2021
 */
class LoadMoreView : FrameLayout, FooterView {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        init()
    }

    private var imageLoadMore: ImageView? = null
    private var textLoadMore: TextView? = null

    private fun init() {

        val linearRoot = LinearLayout(context)
        linearRoot.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        linearRoot.orientation = LinearLayout.HORIZONTAL

        imageLoadMore = ImageView(context).also { img ->
            img.layoutParams = LinearLayout.LayoutParams(
                70,
                70
            ).apply {
                gravity = Gravity.CENTER
            }
            img.setImageDrawable(
                ResourcesCompat.getDrawable(
                    resources,
                    R.drawable.ic_loading,
                    null
                )
            )
        }
        linearRoot.addView(imageLoadMore)
        addView(linearRoot,LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        ))
    }

    override fun begin() {
    }

    override fun progress(progress: Float, all: Float) {
        loadingUI()
    }

    override fun finishing(progress: Float, all: Float) {
        imageLoadMore?.clearAnimation()
    }

    fun loadingUI(){
        textLoadMore?.text = "loading..."
        imageLoadMore?.setImageDrawable(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.ic_loading,
                null
            )
        )
        val rotateAnim = RotateAnimation(
            0F,
            360F,
            Animation.RELATIVE_TO_SELF,
            0.5F,
            Animation.RELATIVE_TO_SELF,
            0.5F
        )
        rotateAnim.interpolator = LinearInterpolator()
        rotateAnim.duration = 2000
        rotateAnim.repeatCount = Animation.RESTART
        imageLoadMore?.startAnimation(rotateAnim)
    }

    override fun loading() {
        loadingUI()
    }

    override fun normal() {
        imageLoadMore?.clearAnimation()
    }

    override fun getView(): View {
        return this
    }
}