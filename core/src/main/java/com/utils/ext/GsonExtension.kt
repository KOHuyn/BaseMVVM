package com.utils.ext

import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken


inline fun <reified T> Gson.fromJson(json: JsonElement) =
    fromJson<T>(json, object : TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJsonSafe(json: JsonElement?): T? {
    if (json == null) return null
    return try {
        fromJson<T>(json, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        e.log()
        null
    }
}

inline fun <reified T> Gson.fromJsonSafe(json: String?): T? {
    if (json == null) return null
    return try {
        fromJson<T>(json, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        e.log()
        null
    }
}

inline fun <reified T> Gson.fromJsonSafe(json: JsonElement?, exception: (Exception) -> Unit): T? {
    if (json == null) return null
    return try {
        fromJson<T>(json, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        e.log()
        exception(e)
        null
    }
}

inline fun <reified T> Gson.fromJsonSafe(json: String?, exception: (Exception) -> Unit): T? {
    if (json == null) return null
    return try {
        fromJson<T>(json, object : TypeToken<T>() {}.type)
    } catch (e: Exception) {
        e.log()
        exception(e)
        null
    }
}