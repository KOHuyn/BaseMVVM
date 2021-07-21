package com.kohuyn.basemvvm.data.model

/**
 * Created by KO Huyn on 21/07/2021.
 */
data class Page(
    var isNextPage: Boolean = true,
    var currentPage: Int = 1,
    var isLoading: Boolean = false
) {
    fun onLoadMore(action: (nextPage: Int) -> Unit) {
        if (isNextPage && !isLoading) {
            isLoading = true
            currentPage++
            action(currentPage)
        }
    }

    fun applyPageResponse(pageResponse:Page){
        isNextPage = pageResponse.isNextPage
        currentPage = pageResponse.currentPage
        isLoading = pageResponse.isLoading
    }
}