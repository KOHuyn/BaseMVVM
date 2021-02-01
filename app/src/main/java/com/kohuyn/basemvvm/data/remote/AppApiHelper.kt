package com.kohuyn.basemvvm.data.remote

import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.rx2androidnetworking.Rx2AndroidNetworking
import io.reactivex.Single

/**
 * Created by KOHuyn on 1/29/2021
 */
class AppApiHelper : ApiHelper {
    override fun getAllUser(): Single<JsonArray> {
        return Rx2AndroidNetworking.get(ApiEndPoint.LIST_USER_URL)
            .build()
            .getObjectSingle(JsonArray::class.java)
    }

    override fun getRepoUser(userName: String): Single<JsonArray> {
        return Rx2AndroidNetworking.get(ApiEndPoint.REPO_URL)
            .addPathParameter(ApiContains.ID, userName)
            .build()
            .getObjectSingle(JsonArray::class.java)
    }
}