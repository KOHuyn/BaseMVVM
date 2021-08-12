package com.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.core.BaseViewHolder
import com.core.databinding.ItemBaseErrorBinding
import com.core.databinding.ItemBaseLoadMoreBinding
import com.core.databinding.ItemBaseLoadingBinding
import com.utils.applyClickShrink
import com.utils.ext.setVisibility

/**
 * Created by KO Huyn on 04/08/2021.
 */
abstract class BaseAdapter<V : ViewBinding> :
    RecyclerView.Adapter<BaseViewHolder<ViewBinding>>() {

    var items = mutableListOf<BaseItemRecyclerView>()
        private set

    open fun <T : BaseItemRecyclerView> addDataLoadMore(items: MutableList<T>) {
        if (items.isNotEmpty()) {
            val countLastPos = itemCount
            this.items.addAll(items)
            notifyItemInserted(countLastPos)
            notifyItemRangeInserted(countLastPos, itemCount)
        }
    }

    open fun <T : BaseItemRecyclerView> setData(items: MutableList<T>) {
        if (items.isNotEmpty()) {
            this.items.removeAll { it !is StatusRecyclerView }
            this.items.addAll(items)
            notifyDataSetChanged()
        }
    }

    fun removeAllItem() {
        items.clear()
        notifyDataSetChanged()
    }

    abstract fun onCreateChildViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<ViewBinding>

    abstract fun onBindChildViewHolder(holder: BaseViewHolder<V>, position: Int)

    fun delete(position: Int) {
        try {
            if (position != RecyclerView.NO_POSITION && items.size > position) {
                items.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, items.size)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private var refreshLayout: SwipeRefreshLayout? = null
    fun attachWithRefreshLayout(refreshLayout: SwipeRefreshLayout) {
        this.refreshLayout = refreshLayout
    }

    fun updateStatusAdapter(statusType: StatusRecyclerView) {
        if (statusType is StatusRecyclerView.Loading) {
            if (statusType.typeLoading == StatusRecyclerView.TypeLoading.REFRESH) {
                if (refreshLayout != null) {
                    refreshLayout?.isRefreshing = statusType.isLoading
                    return
                }
            }
            if (!statusType.isLoading) {
                cancelStatusAdapter(statusType)
                return
            }
        }
        val itemStatusOld = items.find { it is StatusRecyclerView }
        if (itemStatusOld != null) {
            val index = items.indexOf(itemStatusOld)
            if (index < itemCount - 1) {
                delete(index)
                insertStatus(statusType)
            } else {
                if (statusType.itemType == itemStatusOld.itemType) return
                items[index] = statusType
                notifyItemChanged(index)
                notifyItemRangeChanged(index, itemCount)
            }
        } else {
            insertStatus(statusType)
        }
    }

    private fun insertStatus(status: StatusRecyclerView) {
        val lasPos = itemCount
        items.add(lasPos, status)
        notifyItemInserted(lasPos)
        notifyItemRangeInserted(lasPos, itemCount)
    }

    private fun cancelStatusAdapter(statusType: StatusRecyclerView? = null) {
        val itemStatusOld = items.find { it is StatusRecyclerView } ?: return
        if (statusType != null) {
            if (itemStatusOld.itemType != statusType.itemType) return
        }
        val posLoading = items.indexOf(itemStatusOld)
        delete(posLoading)
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].itemType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<ViewBinding> {
        return when (viewType) {
            BaseItemRecyclerView.TYPE_LOADING -> BaseViewHolder(
                ItemBaseLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            BaseItemRecyclerView.TYPE_LOAD_MORE -> BaseViewHolder(
                ItemBaseLoadMoreBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            BaseItemRecyclerView.TYPE_ERROR -> BaseViewHolder(
                ItemBaseErrorBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> onCreateChildViewHolder(parent, viewType)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder<ViewBinding>, position: Int) {
        when (getItemViewType(position)) {
            BaseItemRecyclerView.TYPE_ERROR -> {
                val item = items[position] as StatusRecyclerView.Error
                with(holder as BaseViewHolder<ItemBaseErrorBinding>) {
                    item.msg?.let { binding.txtMsg.text = it }
                    binding.txtMsg.setVisibility(item.msg != null)
                    binding.imgNoData.setVisibility(item.src != null)
                    item.src?.let { binding.imgNoData.setImageResource(it) }
                    binding.btnRetry.setVisibility(item.showButton)
                    binding.btnRetry.applyClickShrink()
                    item.textButton?.let { binding.btnRetry.text = it }
                    binding.btnRetry.setOnClickListener {
                        onActionWhenErrorCallback?.setOnActionWhenErrorListener(
                            item.resultCode
                        )
                    }
                }
            }
            in 0..BaseItemRecyclerView.TYPE_0_IN_NORMAL -> {
                onBindChildViewHolder(holder as BaseViewHolder<V>, position)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    fun onActionWhenErrorListener(callback: (resultCode: Int) -> Unit) {
        onActionWhenErrorCallback = OnActionWhenErrorCallback(callback)
    }

    private fun interface OnActionWhenErrorCallback {
        fun setOnActionWhenErrorListener(resultCode: Int)
    }

    private var onActionWhenErrorCallback: OnActionWhenErrorCallback? = null
}