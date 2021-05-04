package com.core

import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

class BaseViewHolder<T : ViewBinding>(view: T) : RecyclerView.ViewHolder(view.root) {
    val binding = view
}