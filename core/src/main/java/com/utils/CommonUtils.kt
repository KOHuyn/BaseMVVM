package com.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import java.io.IOException
import java.nio.charset.StandardCharsets

object CommonUtils {
    @SuppressLint("all")
    fun getDeviceId(context: Context): String? {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    @Throws(IOException::class)
    fun loadJSONFromAsset(context: Context, jsonFileName: String?): String? {
        val manager = context.assets
        val inputStream = manager.open(jsonFileName!!)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.close()
        return String(buffer, StandardCharsets.UTF_8)
    }
}