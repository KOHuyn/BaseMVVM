package com.kohuyn.basemvvm.data

import com.google.gson.JsonArray
import com.kohuyn.basemvvm.data.local.prefs.PrefsHelper
import com.kohuyn.basemvvm.data.remote.fan.ApiHelper
import io.reactivex.Single

/**
 * Created by KOHuyn on 1/29/2021
 */
class AppDataManager(private val apiHelper: ApiHelper, private val prefsHelper: PrefsHelper) :
    DataManager {
    override fun getAllUser(): Single<JsonArray> = apiHelper.getAllUser()
    override fun getRepoUser(userName: String): Single<JsonArray> = apiHelper.getRepoUser(userName)
}