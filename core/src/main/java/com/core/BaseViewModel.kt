package com.core

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.core.adapter.StatusRecyclerView
import com.utils.SchedulerProvider
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<DATA>(
    val dataManager: DATA,
    val schedulerProvider: SchedulerProvider
) : ViewModel() {
    val isLoading: PublishSubject<Boolean> = PublishSubject.create()
    val rxMessage: PublishSubject<String> = PublishSubject.create()

    companion object {
        private const val PAGE_DEFAULT = 1
        private const val INTEGER_DEFAULT = 1
    }

    fun PublishSubject<StatusRecyclerView>.onStatusLoading(
        isLoading: Boolean,
        page: Int = PAGE_DEFAULT, isRefresh: Boolean = false
    ) {
        onNext(
            StatusRecyclerView.Loading(
                isLoading,
                if (page == PAGE_DEFAULT) {
                    if (isRefresh) {
                        StatusRecyclerView.TypeLoading.REFRESH
                    } else StatusRecyclerView.TypeLoading.LOADING
                } else StatusRecyclerView.TypeLoading.LOAD_MORE
            )
        )
    }

    fun PublishSubject<StatusRecyclerView>.onStatusError(
        msg: String? = null,
        @DrawableRes src: Int? = R.drawable.no_data_img,
        showButton: Boolean = false,
        textButton: String? = null,
        resultCode: Int = INTEGER_DEFAULT
    ) {
        onNext(StatusRecyclerView.Error(msg, src, showButton, textButton, resultCode))
    }
}