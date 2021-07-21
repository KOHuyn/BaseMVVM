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
        const val KEY_COUNT_OPEN_APP = "KEY_COUNT_OPEN_APP"
    }

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)

    override var countOpenApp: Int
        get() = sharedPreferences.getInt(KEY_COUNT_OPEN_APP, 1)
        set(value) {
            sharedPreferences.edit().putInt(KEY_COUNT_OPEN_APP, value).apply()
        }
}