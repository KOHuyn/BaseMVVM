package com.kohuyn.basemvvm.di

import androidx.room.Room
import com.google.gson.GsonBuilder
import com.kohuyn.basemvvm.data.AppDataManager
import com.kohuyn.basemvvm.data.DataManager
import com.kohuyn.basemvvm.data.local.db.AppDatabase
import com.kohuyn.basemvvm.data.local.db.AppDbHelper
import com.kohuyn.basemvvm.data.local.db.DbHelper
import com.kohuyn.basemvvm.data.local.prefs.AppPrefsHelper
import com.kohuyn.basemvvm.data.local.prefs.PrefsHelper
import com.kohuyn.basemvvm.data.remote.ApiHelper
import com.kohuyn.basemvvm.data.remote.AppApiHelper
import com.utils.SchedulerProvider
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Created by KOHuyn on 1/29/2021
 */

val app_module: Module = module {
    single { SchedulerProvider() }

    single { AppPrefsHelper(get(), "Kohuyn", get()) as PrefsHelper }

    single { AppApiHelper() as ApiHelper }

    single { AppDbHelper(get()) as DbHelper }

    single { Room.databaseBuilder(get(),AppDatabase::class.java,"db_mvvm.sqlite").build() }

    single { AppDataManager(get(), get(), get()) as DataManager }

    single { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()!! }

}

val baseModule = listOf(app_module, view_module)