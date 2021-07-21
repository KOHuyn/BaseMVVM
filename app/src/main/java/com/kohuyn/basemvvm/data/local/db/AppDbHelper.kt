package com.kohuyn.basemvvm.data.local.db

import com.kohuyn.basemvvm.data.model.User
import io.reactivex.Observable

/**
 * Created by KOHuyn on 1/29/2021
 */
class AppDbHelper constructor(private val appDatabase: AppDatabase) : DbHelper {
    override fun saveAllUser(users: List<User>): Observable<List<Long>> =
        Observable.fromCallable { appDatabase.userDAO().insertAllInDb(users) }

    override fun getAllUserInDb(): Observable<List<User>> = Observable.fromCallable {
        appDatabase.userDAO().getAllUserInDb()
    }

    override fun getUserLimit(): Observable<List<User>> = Observable.fromCallable {
        appDatabase.userDAO().getUserInDbLimit()
    }
}