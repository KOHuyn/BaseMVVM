package com.kohuyn.basemvvm.data.remote.retrofit

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.kohuyn.basemvvm.data.model.User

class AppApiHelper(private val apiService: ApiService) : ApiHelper {
    override suspend fun getUsers(since: String): JsonArray = apiService.getUsers(since)
}