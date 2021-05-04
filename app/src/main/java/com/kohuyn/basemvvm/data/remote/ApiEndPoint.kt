package com.kohuyn.basemvvm.data.remote

/**
 * Created by KOHuyn on 1/29/2021
 */
object ApiEndPoint {
    const val BASE_URL = "https://api.github.com/"
    const val LIST_USER_URL = "https://api.github.com/users?since=XXX"
    const val REPO_URL = "https://api.github.com/users/{${ApiContains.ID}}/repos"
}