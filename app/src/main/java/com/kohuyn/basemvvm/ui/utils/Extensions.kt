package com.kohuyn.basemvvm.ui.utils

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.kohuyn.basemvvm.MyApplication
import com.kohuyn.basemvvm.R

/**
 * Created by KOHuyn on 2/1/2021
 */

fun ImageView.show(url: String?) {
    val requestOptions = RequestOptions()
        .error(R.color.colorPrimary)
        .placeholder(R.color.colorPrimary)
        .centerCrop()
    GlideApp.with(MyApplication.getInstance())
        .load(url).apply(requestOptions)
        .into(this)
}