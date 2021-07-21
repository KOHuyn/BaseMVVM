package com.kohuyn.basemvvm.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kohuyn.basemvvm.data.model.User
import io.reactivex.Observable

/**
 * Created by KO Huyn on 21/07/2021.
 */
@Dao
interface UserDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllInDb(listUser: List<User>): List<Long>

    @Query("select * from user")
    fun getAllUserInDb(): List<User>

    @Query("select * from user limit 10")
    fun getUserInDbLimit(): List<User>
}