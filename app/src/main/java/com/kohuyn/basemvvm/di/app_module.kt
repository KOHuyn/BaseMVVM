package com.kohuyn.basemvvm.di

import com.google.gson.GsonBuilder
import com.kohuyn.basemvvm.data.AppDataManager
import com.kohuyn.basemvvm.data.DataManager
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

    single { AppDataManager(get(), get()) as DataManager }

    single { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()!! }
}

val baseModule = listOf(app_module, view_module)