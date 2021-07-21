package com.kohuyn.basemvvm.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kohuyn.basemvvm.data.local.db.dao.UserDAO
import com.kohuyn.basemvvm.data.model.User

/**
 * Created by KOHuyn on 1/29/2021
 */
@Database(entities = [User::class], exportSchema = false, version = 1)
abstract class AppDatabase:RoomDatabase() {
    abstract fun userDAO(): UserDAO
}