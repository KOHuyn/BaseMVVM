package com.kohuyn.basemvvm.ui.main

import com.core.BaseViewModel
import com.google.gson.Gson
import com.kohuyn.basemvvm.data.DataManager
import com.kohuyn.basemvvm.data.model.Page
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.ui.getErrorMsg
import com.utils.SchedulerProvider
import com.utils.ext.fromJsonSafe
import com.utils.ext.log
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by KOHuyn on 1/29/2021
 */
class MainViewModel(
    dataManager: DataManager, schedulerProvider: SchedulerProvider,
    private val gson: Gson
) : BaseViewModel<DataManager>(dataManager, schedulerProvider) {

    init {
        getUserFromDbLimit(true)
    }

    var countOpenApp: Int
        get() = dataManager.countOpenApp
        set(value) {
            dataManager.countOpenApp = value
        }

    val rxUsers: PublishSubject<MutableList<User>> = PublishSubject.create()

    val rxUsersWithPage: PublishSubject<Pair<MutableList<User>, Page>> = PublishSubject.create()

    val rxIsSaveDb: PublishSubject<MutableList<User>> = PublishSubject.create()

    val rxUserInDbAvailable: PublishSubject<Boolean> = PublishSubject.create()

    fun getAllUserApi(): Disposable {
        isLoading.onNext(true)
        return dataManager.getAllUser()
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .subscribe({ response ->
                isLoading.onNext(false)
                if (response.toString() != "[]") {
                    gson.fromJsonSafe<MutableList<User>>(response)?.let {
                        rxUsers.onNext(it)
                        rxIsSaveDb.onNext(it)
                    }
                        ?: rxUsers.onNext(mutableListOf())
                } else {
                    rxUsers.onNext(mutableListOf())
                }
            }, {
                isLoading.onNext(false)
                rxMessage.onNext(it.getErrorMsg())
                rxUsers.onNext(mutableListOf())
                it.log()
            })
    }

    fun saveAllUserInDb(users: List<User>): Disposable {
        return dataManager.saveAllUser(users)
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe({
                rxMessage.onNext("Lưu dữ liệu vào db thành công")
            }, {
                rxMessage.onNext("Lưu dữ liệu vào db thất bại")
                it.log()
            })
    }

    fun getUserFromDbLimit(isCheckAvailable: Boolean = false, page: Int = 1): Disposable {
        isLoading.onNext(true)
        return dataManager.getUserLimit()
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe({
                isLoading.onNext(false)
                if (isCheckAvailable) {
                    rxUserInDbAvailable.onNext(it.isNotEmpty())
                } else {
                    rxUsersWithPage.onNext(
                        Pair(
                            it.toMutableList(),
                            Page(true, page, false)
                        )
                    )
                }
            }, {
                isLoading.onNext(false)
                rxUsersWithPage.onNext(Pair(mutableListOf(), Page(true, page, false)))
                it.log()
            })
    }
}