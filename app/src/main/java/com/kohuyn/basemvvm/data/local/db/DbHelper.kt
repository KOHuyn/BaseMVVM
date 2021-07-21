package com.kohuyn.basemvvm.data.local.db

import com.kohuyn.basemvvm.data.model.User
import io.reactivex.Observable

/**
 * Created by KOHuyn on 1/29/2021
 */
interface DbHelper {
    fun saveAllUser(users: List<User>): Observable<List<Long>>
    fun getAllUserInDb(): Observable<List<User>>
    fun getUserLimit(): Observable<List<User>>
}