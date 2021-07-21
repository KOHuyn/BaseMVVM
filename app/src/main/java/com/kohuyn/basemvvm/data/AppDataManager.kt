package com.kohuyn.basemvvm.data

import com.google.gson.JsonArray
import com.kohuyn.basemvvm.data.local.db.DbHelper
import com.kohuyn.basemvvm.data.local.prefs.PrefsHelper
import com.kohuyn.basemvvm.data.model.User
import com.kohuyn.basemvvm.data.remote.ApiHelper
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by KOHuyn on 1/29/2021
 */
class AppDataManager(
    private val apiHelper: ApiHelper,
    private val prefsHelper: PrefsHelper,
    private val dbHelper: DbHelper
) :
    DataManager {
    override fun getAllUser(): Single<JsonArray> = apiHelper.getAllUser()

    override fun getRepoUser(userName: String): Single<JsonArray> = apiHelper.getRepoUser(userName)

    override var countOpenApp: Int
        get() = prefsHelper.countOpenApp
        set(value) {
            prefsHelper.countOpenApp = value
        }

    override fun saveAllUser(users: List<User>): Observable<List<Long>> =
        dbHelper.saveAllUser(users)

    override fun getAllUserInDb(): Observable<List<User>> = dbHelper.getAllUserInDb()

    override fun getUserLimit(): Observable<List<User>> = dbHelper.getUserLimit()
}