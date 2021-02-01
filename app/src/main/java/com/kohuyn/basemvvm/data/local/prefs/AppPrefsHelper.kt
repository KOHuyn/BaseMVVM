package com.kohuyn.basemvvm.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

/**
 * Created by KOHuyn on 1/29/2021
 */
class AppPrefsHelper constructor(context: Context, prefsName: String, private val gson: Gson) :
    PrefsHelper {
    companion object {
        const val KEY_USER = "USER"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
}