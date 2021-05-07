package com.kohuyn.basemvvm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.BaseViewModel
import com.google.gson.Gson
import com.kohuyn.basemvvm.data.DataManager
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.data.remote.retrofit.ApiHelper
import com.kohuyn.basemvvm.data.remote.retrofit.ApiService
import com.kohuyn.basemvvm.ui.getErrorMsg
import com.utils.SchedulerProvider
import com.utils.ext.fromJson
import com.utils.ext.fromJsonSafe
import com.utils.ext.log
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

/**
 * Created by KOHuyn on 1/29/2021
 */
class MainViewModel(
    private val apiHelper: ApiHelper,
    private val gson: Gson
) :
    ViewModel() {

    val rxUsers: PublishSubject<List<User>> = PublishSubject.create()
    val isLoading: PublishSubject<Boolean> = PublishSubject.create()
    val rxMessage: PublishSubject<String> = PublishSubject.create()

//    fun getAllUser(): Disposable {
//        isLoading.onNext(true)
//        return dataManager.getAllUser()
//            .compose(schedulerProvider.ioToMainSingleScheduler())
//            .subscribe({ response ->
//                isLoading.onNext(false)
//                if (response.toString() != "[]") {
//                    try {
//                        val users = gson.fromJson<List<User>>(response)
//                        rxUsers.onNext(users)
//                    } catch (e: Exception) {
//                        e.log()
//                    }
//                } else {
//                    rxUsers.onNext(emptyList())
//                }
//            }, {
//                isLoading.onNext(false)
//                rxMessage.onNext(it.getErrorMsg())
//                it.log()
//            })
//    }

    fun getAllUser() {
        viewModelScope.launch {
            isLoading.onNext(true)
            val response = apiHelper.getUsers("XXX")
            if (response.toString() != "[]") {
                isLoading.onNext(false)
                try {
                    val users = gson.fromJson<List<User>>(response)
                    rxUsers.onNext(users)
                } catch (e: Exception) {
                    e.log()
                }
            } else {
                rxUsers.onNext(emptyList())
            }
        }
    }
}