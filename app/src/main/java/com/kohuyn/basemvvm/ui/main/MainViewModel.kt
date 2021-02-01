package com.kohuyn.basemvvm.ui.main

import com.core.BaseViewModel
import com.google.gson.Gson
import com.kohuyn.basemvvm.data.DataManager
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.ui.getErrorMsg
import com.utils.SchedulerProvider
import com.utils.ext.log
import com.utils.ext.toList
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by KOHuyn on 1/29/2021
 */
class MainViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    private val gson: Gson
) :
    BaseViewModel<DataManager>(dataManager, schedulerProvider) {

    val rxUsers: PublishSubject<List<User>> = PublishSubject.create()

    fun getAllUser(): Disposable {
        isLoading.onNext(true)
        return dataManager.getAllUser()
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .subscribe({ response ->
                isLoading.onNext(false)
                if (response.toString() != "[]") {
                    try {
                        val users: List<User> = gson.toList(response)
                        rxUsers.onNext(users)
                    } catch (e: Exception) {
                        e.log()
                    }
                } else {
                    rxUsers.onNext(emptyList())
                }
            }, {
                isLoading.onNext(false)
                rxMessage.onNext(it.getErrorMsg())
                it.log()
            })
    }
}