package com.kohuyn.basemvvm.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.core.BaseViewModel
import com.google.gson.Gson
import com.google.gson.JsonElement
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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
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
    val rxNoDataCallback: PublishSubject<Boolean> = PublishSubject.create()

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

    fun getAllUserSafe() {
        isLoading.onNext(true)
        viewModelScope.launchSafe({
            val response = apiHelper.getUsers("xxx")
            val user = gson.fromJsonSafe<List<User>>(response)
            isLoading.onNext(false)
            if (user != null) {
                rxUsers.onNext(user)
            } else {
                rxNoDataCallback.onNext(true)
                rxMessage.onNext("Oops,No data")
            }
        }, { throwable ->
            isLoading.onNext(false)
            rxNoDataCallback.onNext(true)
            when (throwable) {
                is IOException -> rxMessage.onNext("No InternetConnect")
                is HttpException -> rxMessage.onNext("${throwable.code()} - ${throwable.message()}")
                else -> rxMessage.onNext("Oops,ERROR T.T")
            }
        })
    }

    fun CoroutineScope.launchSafe(
        block: suspend CoroutineScope.() -> Unit,
        throwableHandle: (exception: Throwable) -> Unit
    ): Job {
        return launch(CoroutineExceptionHandler { _, throwable ->
            throwableHandle(throwable)
        }) {
            block()
        }
    }
}