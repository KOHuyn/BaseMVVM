package com.kohuyn.basemvvm.data.remote.retrofit

import com.google.gson.JsonArray

interface ApiHelper {
    suspend fun getUsers(): JsonArray
}