package com.core

import androidx.lifecycle.ViewModel
import com.utils.SchedulerProvider
import io.reactivex.subjects.PublishSubject

abstract class BaseViewModel<DATA>(
    val dataManager: DATA,
    val schedulerProvider: SchedulerProvider
) : ViewModel() {
    val isLoading: PublishSubject<Boolean> = PublishSubject.create()
    val rxMessage: PublishSubject<String> = PublishSubject.create()
}