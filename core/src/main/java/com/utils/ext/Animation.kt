package com.utils.ext

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.text.Editable
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import android.widget.EditText

/**
 * Created by KO Huyn on 21/07/2021.
 */

fun View.visibleWithFade(duration: Long = 300) {
    val view = this
    view.alpha = 0f
    view.visibility = View.VISIBLE
    view.animate()
        .alpha(1f)
        .setDuration(duration)
        .setListener(null)
}

fun View.goneWithFade(duration: Long = 300) {
    val view = this
    view.animate()
        .alpha(0f)
        .setDuration(duration)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                view.visibility = View.GONE
            }
        })
}


fun View.slideDown() {
    // Prepare the View for the animation
    this.visibility = View.VISIBLE
    this.alpha = 0.0f

// Start the animation
    this.animate()
        .translationY(this.height.toFloat())
        .alpha(1.0f)
        .setListener(null)
}

fun View.slideUp() {
    val view = this
    view.animate()
        .translationY(0.0f)
        .alpha(0.0f)
        .setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                view.visibility = View.VISIBLE
            }
        })
}


fun View.changeBackgroundColorWithAnim(
    fromColor: Int,
    toColor: Int
) {
    val view = this
    val valueAnimator =
        ValueAnimator.ofObject(ArgbEvaluator(), fromColor, toColor)
    valueAnimator.duration = 700
    valueAnimator.addUpdateListener { animation -> view.setBackgroundColor((animation.animatedValue as Int)) }
    valueAnimator.start()
}

fun View.changeWidth(endWidth: Int, duration: Long = 400, onComplete: (() -> Unit)? = null) {
    val anim = ValueAnimator.ofInt(this.measuredWidth, endWidth)
    anim.addUpdateListener { valueAnimator ->
        val `val` = valueAnimator.animatedValue as Int
        val layoutParams: ViewGroup.LayoutParams = layoutParams
        layoutParams.width = `val`
        setLayoutParams(layoutParams)
    }
    anim.duration = duration
    onComplete?.let {
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                onComplete()
            }

            override fun onAnimationCancel(animation: Animator?) {}

            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }
    anim.start()
}

fun View.expand() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = 1
    visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.height =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = (targetHeight / context.resources.displayMetrics.density).toLong()
    startAnimation(a)
}

fun View.collapse() {
    val initialHeight = measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = (initialHeight / context.resources.displayMetrics.density).toLong()
    startAnimation(a)
}

fun View.popEnter() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((parent as View).height, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetWidth = measuredWidth

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.width = 1
    visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            layoutParams.width =
                if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT else (targetWidth * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = (targetWidth / context.resources.displayMetrics.density).toLong()
    startAnimation(a)
}

fun View.popExit() {
    val initialWidth = measuredWidth
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation?) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.width = initialWidth - (initialWidth * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = (initialWidth / context.resources.displayMetrics.density).toLong()
    startAnimation(a)
}

fun View.setVisibleHorizontal(isVisible: Boolean) {
    if (isVisible) popEnter() else popExit()
}

fun View.setVisibleVertical(isVisible: Boolean) {
    if (isVisible) expand() else collapse()
}
