package com.utils.ext

import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import com.core.R

fun View.beInvisibleIf(beInvisible: Boolean) = if (beInvisible) beInvisible() else beVisible()

fun View.beVisibleIf(beVisible: Boolean) = if (beVisible) beVisible() else beGone()

fun View.beGoneIf(beGone: Boolean) = beVisibleIf(!beGone)

fun View.beInvisible() {
    visibility = View.INVISIBLE
}

fun View.beVisible() {
    visibility = View.VISIBLE
}

fun View.beGone() {
    visibility = View.GONE
}

var View.isVisible: Boolean
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }
    get() = visibility == View.VISIBLE

fun View.onGlobalLayout(callback: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            viewTreeObserver.removeOnGlobalLayoutListener(this)
            callback()
        }
    })
}

var View.isInvisible: Boolean
    get() = visibility == View.INVISIBLE
    set(value) {
        visibility = if (value) View.INVISIBLE else View.VISIBLE
    }

var View.isGone: Boolean
    get() = visibility == View.GONE
    set(value) {
        visibility = if (value) View.GONE else View.VISIBLE
    }

fun View.performHapticFeedback() = performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

var View.isEnablez: Boolean
    get() = this.isEnabled
    set(value) {
        alpha = if (value) 1.0F else 0.5F
        isEnabled = value
    }

var EditText.value: String?
    set(value) {
        text = Editable.Factory.getInstance().newEditable(value ?: "")
    }
    get() = text.toString()


fun TextView.clickAreaWith(fromPos: Int?, toPos: Int?, action: (v: View) -> Unit) {
    val spanStr = SpannableString(text.toString())
    val clickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            action(widget)
        }

        override fun updateDrawState(ds: TextPaint) {
            super.updateDrawState(ds)
            ds.isUnderlineText = false
        }
    }
    try {
        if (fromPos != null && toPos != null) {
            spanStr.setSpan(clickableSpan, fromPos, toPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    text = spanStr
    movementMethod = LinkMovementMethod.getInstance()
    highlightColor = ResourcesCompat.getColor(resources, R.color.colorLink, null)
}

fun TextView.setTextNotNull(text: String?) {
    if (text == null || text == "" || text == "null") {
        visibility = View.GONE
    } else {
        this.text = text.toString()
        visibility = View.VISIBLE
    }
}