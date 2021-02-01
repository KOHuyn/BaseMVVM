package com.kohuyn.basemvvm.ui

import com.androidnetworking.error.ANError
import java.net.HttpURLConnection

/**
 * Created by KOHuyn on 2/1/2021
 */

fun Throwable?.getErrorMsg(): String = try {
    when {
        this is ANError -> {
            when (this.errorCode) {
                HttpURLConnection.HTTP_INTERNAL_ERROR -> "Internal Server Error."
                HttpURLConnection.HTTP_GATEWAY_TIMEOUT -> "Gateway Timeout."
                HttpURLConnection.HTTP_CLIENT_TIMEOUT -> "Request Time-Out."
                HttpURLConnection.HTTP_NOT_FOUND -> "Not Found."
                HttpURLConnection.HTTP_UNAUTHORIZED -> "Unauthorized."
                HttpURLConnection.HTTP_BAD_REQUEST -> "Bad Request."
                0 -> {
                    if (this.errorDetail == "connectionError") "NoInternet."
                    else "Server error."
                }
                else -> "Server error."
            }
        }
        this?.message != null -> this.message!!
        else -> "Server error."
    }
} catch (e: Exception) {
    "Server error."
}