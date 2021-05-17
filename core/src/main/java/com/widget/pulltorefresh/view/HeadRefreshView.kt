package com.widget.pulltorefresh.view

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Looper
import android.util.AttributeSet
import android.util.Log
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
import java.util.logging.Handler

/**
 *Created by KO Huyn on 5/9/2021
 */
class HeadRefreshView : FrameLayout, HeadView {
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
    private var drawableLoading: Drawable? = null

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
//
//        val space = Space(context)
//        space.layoutParams = LinearLayout.LayoutParams(
//            50,
//            0
//        )
//        linearRoot.addView(space)
//
//        textLoadMore = TextView(context).also { text ->
//            text.layoutParams = LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT
//            ).apply {
//                gravity = Gravity.CENTER
//            }
//            text.text = "Loading..."
//            text.setTextColor(
//                ResourcesCompat.getColor(
//                    resources,
//                    android.R.color.darker_gray,
//                    null
//                )
//            )
//            text.textSize = 20F
//        }
//        linearRoot.addView(textLoadMore)

        addView(
            linearRoot, LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT,
                Gravity.CENTER
            )
        )
    }

    override fun begin() {

    }

    override fun progress(progress: Float, all: Float) {
        val s = progress / all
        imageLoadMore?.clearAnimation()
        imageLoadMore?.rotation = if (s >= 0.9) 180F else 0F
        textLoadMore?.text = if (progress >= all - 10) "Thả để làm mới" else "Vuốt để làm mới"
    }

    override fun finishing(progress: Float, all: Float) {

    }

    override fun loading() {
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

    override fun normal() {
        textLoadMore?.text = ""
    }

    override fun getView(): View {
        return this
    }
}