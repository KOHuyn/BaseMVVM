package com.kohuyn.basemvvm.data.remote.retrofit

import com.google.gson.JsonArray
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun getUsers(): JsonArray
}