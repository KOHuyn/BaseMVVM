package com.core.adapter

import android.widget.Filter
import java.util.*

/**
 * Created by KO Huyn on 12/08/2021.
 */
abstract class BaseValueFilter<I : BaseItemRecyclerView>(
    private var items: MutableList<I>,
) : Filter() {
    private fun isValidFilter(constraint: CharSequence?, item: I): Boolean {
        var isValid = false
        val arrKey = (item as? KeyFilterRecyclerView)?.keysFilter?.filterNotNull() ?: return false
        for (key in arrKey) {
            if ((key.toUpperCase(Locale.ROOT).replaceText()).contains(
                    constraint.toString().toUpperCase(Locale.ROOT).replaceText()
                )
            ) {
                isValid = true
            }
        }
        return isValid
    }

    override fun performFiltering(constraint: CharSequence?): FilterResults {
        val filterResult = FilterResults()
        if (constraint != null && constraint.isNotEmpty()) {
            val filterList = mutableListOf<I>()
            for (i in items.indices) {
                if (isValidFilter(constraint, items[i])) {
                    filterList.add(items[i])
                }
            }
            filterResult.count = filterList.size
            filterResult.values = filterList
        } else {
            filterResult.count = items.size
            filterResult.values = items
        }
        return filterResult
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        onResultData(results?.values as MutableList<I>)
    }

    abstract fun onResultData(data: MutableList<I>)

    private fun String.replaceText(): String {
        var str = this
        str = str.replace("[àáạảãâầấậẩẫăằắặẳẵ]".toRegex(), "a")
        str = str.replace("[èéẹẻẽêềếệểễ]".toRegex(), "e")
        str = str.replace("[ìíịỉĩ]".toRegex(), "i")
        str = str.replace("[òóọỏõôồốộổỗơờớợởỡ]".toRegex(), "o")
        str = str.replace("[ùúụủũưừứựửữ]".toRegex(), "u")
        str = str.replace("[ỳýỵỷỹ]".toRegex(), "y")
        str = str.replace("đ".toRegex(), "d")

        str = str.replace("[ÀÁẠẢÃÂẦẤẬẨẪĂẰẮẶẲẴ]".toRegex(), "A")
        str = str.replace("[ÈÉẸẺẼÊỀẾỆỂỄ]".toRegex(), "E")
        str = str.replace("[ÌÍỊỈĨ]".toRegex(), "I")
        str = str.replace("[ÒÓỌỎÕÔỒỐỘỔỖƠỜỚỢỞỠ]".toRegex(), "O")
        str = str.replace("[ÙÚỤỦŨƯỪỨỰỬỮ]".toRegex(), "U")
        str = str.replace("[ỲÝỴỶỸ]".toRegex(), "Y")
        str = str.replace("Đ".toRegex(), "D")
        return str
    }
}