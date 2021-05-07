package com.kohuyn.basemvvm.data.remote.retrofit

import com.google.gson.JsonArray
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(@Query("since") since:String): JsonArray
}