package com.kohuyn.basemvvm.ui.repo

import com.core.BaseViewModel
import com.google.gson.Gson
import com.kohuyn.basemvvm.data.DataManager
import com.kohuyn.basemvvm.data.model.Repository
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.ui.getErrorMsg
import com.utils.SchedulerProvider
import com.utils.ext.fromJson
import com.utils.ext.fromJsonSafe
import com.utils.ext.log
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

/**
 * Created by KOHuyn on 2/1/2021
 */
class RepositoriesViewModel(
    dataManager: DataManager,
    schedulerProvider: SchedulerProvider,
    private val gson: Gson
) : BaseViewModel<DataManager>(dataManager, schedulerProvider) {

    val rxRepositories: PublishSubject<MutableList<Repository>> = PublishSubject.create()

    fun getRepos(name: String): Disposable {
        isLoading.onNext(true)
        return dataManager.getRepoUser(name)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .subscribe({ response ->
                isLoading.onNext(false)
                if (response.toString() != "[]") {
                    gson.fromJsonSafe<MutableList<Repository>>(response)?.let {
                        rxRepositories.onNext(it)
                    } ?: rxRepositories.onNext(mutableListOf())
                } else {
                    rxRepositories.onNext(mutableListOf())
                }
            }, {
                isLoading.onNext(false)
                rxMessage.onNext(it.getErrorMsg())
                rxRepositories.onNext(mutableListOf())
                it.log()
            })
    }
}