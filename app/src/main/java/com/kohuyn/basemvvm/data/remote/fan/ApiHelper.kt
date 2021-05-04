package com.kohuyn.basemvvm.data.remote.fan

import com.google.gson.JsonArray
import io.reactivex.Single

/**
 * Created by KOHuyn on 1/29/2021
 */
interface ApiHelper {
    fun getAllUser():Single<JsonArray>
    fun getRepoUser(userName:String):Single<JsonArray>
}