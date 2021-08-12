package com.kohuyn.basemvvm.ui.main

import android.os.Handler
import android.os.Looper
import com.core.BaseViewModel
import com.core.adapter.StatusRecyclerView
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

    var countOpenApp: Int
        get() = dataManager.countOpenApp
        set(value) {
            dataManager.countOpenApp = value
        }

    val rxUsers: PublishSubject<MutableList<User>> = PublishSubject.create()

    val rxUsersWithPage: PublishSubject<Pair<MutableList<User>, Page>> = PublishSubject.create()

    val rxIsSaveDb: PublishSubject<MutableList<User>> = PublishSubject.create()

    val rxUserInDbAvailable: PublishSubject<Boolean> = PublishSubject.create()

    val rxStatusRcv: PublishSubject<StatusRecyclerView> = PublishSubject.create()

    fun getAllUserApi(isRefreshing: Boolean): Disposable {
        rxStatusRcv.onStatusLoading(true, isRefresh = isRefreshing)
        return dataManager.getAllUser()
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .subscribe({ response ->
                rxStatusRcv.onStatusLoading(false, isRefresh = isRefreshing)
                if (response.toString() != "[]") {
                    gson.fromJsonSafe<MutableList<User>>(response)?.let {
                        rxUsers.onNext(it)
                        rxIsSaveDb.onNext(it)
                    } ?: rxStatusRcv.onStatusError("Kiểu dữ liệu không phù hợp", null)
                } else {
                    rxStatusRcv.onStatusError("Oops \nKhông có dữ liệu nào cả", showButton = true)
                }
            }, {
                rxStatusRcv.onStatusError(it.getErrorMsg(), null, true)
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

    fun getUserFromDbLimit(
        isCheckAvailable: Boolean = false,
        page: Int = 1,
        isRefreshing: Boolean = false
    ): Disposable {
        rxStatusRcv.onStatusLoading(true, page, isRefreshing)
        return dataManager.getUserLimit()
            .compose(schedulerProvider.ioToMainObservableScheduler())
            .subscribe({
                Handler(Looper.getMainLooper()).postDelayed({
                    rxStatusRcv.onStatusLoading(false, page, isRefreshing)
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
                }, 2000)
            }, {
                rxStatusRcv.onStatusError(it.getErrorMsg(), null, showButton = true)
                it.log()
            })
    }
}