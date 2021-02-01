package com.kohuyn.basemvvm.ui.repo

import com.core.BaseViewModel
import com.google.gson.Gson
import com.kohuyn.basemvvm.data.DataManager
import com.kohuyn.basemvvm.data.model.Repository
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.ui.getErrorMsg
import com.utils.SchedulerProvider
import com.utils.ext.log
import com.utils.ext.toList
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

    val rxRepositories: PublishSubject<List<Repository>> = PublishSubject.create()

    fun getRepos(name: String): Disposable {
        isLoading.onNext(true)
        return dataManager.getRepoUser(name)
            .compose(schedulerProvider.ioToMainSingleScheduler())
            .subscribe({ response ->
                isLoading.onNext(false)
                if (response.toString() != "[]") {
                    try {
                        val repositories: List<Repository> = gson.toList(response)
                        rxRepositories.onNext(repositories)
                    } catch (e: Exception) {
                        e.log()
                    }
                } else {
                    rxRepositories.onNext(emptyList())
                }
            }, {
                isLoading.onNext(false)
                rxMessage.onNext(it.getErrorMsg())
                it.log()
            })
    }
}