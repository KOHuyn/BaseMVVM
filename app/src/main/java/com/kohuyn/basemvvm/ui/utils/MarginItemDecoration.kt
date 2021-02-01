package com.kohuyn.basemvvm.ui.utils


import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.RecyclerView.State

class MarginItemDecoration(
    private val spaceHeight: Int,
    private val isHorizontalRecyclerView: Boolean = false,
    private val isGridLayout: Boolean = false,
    private val spanCount: Int? = null
) : ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: State
    ) {
        with(outRect) {
            val posItem = parent.getChildAdapterPosition(view) + 1
            if (isHorizontalRecyclerView) {
                if (posItem == 1) {
                    left = spaceHeight
                }
                right = spaceHeight
            }
            if (isGridLayout && spanCount != null) {
                if (posItem % spanCount != 0) {
                    right = spaceHeight
                }
                if (posItem <= spanCount) {
                    top = spaceHeight
                }
                bottom = spaceHeight
            }

            if (!isHorizontalRecyclerView && !isGridLayout) {
                if (parent.getChildAdapterPosition(view) == 0) {
                    top = spaceHeight
                }
                bottom = spaceHeight
            }
        }
    }
}