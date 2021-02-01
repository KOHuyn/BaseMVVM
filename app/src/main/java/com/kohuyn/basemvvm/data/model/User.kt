package com.kohuyn.basemvvm.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by KOHuyn on 1/29/2021
 */
class User(
    @Expose
    @SerializedName("login")
    val login: String? = null,
    @Expose
    @SerializedName("id")
    val id: Int? = null,
    @Expose
    @SerializedName("node_id")
    val nodeId: String? = null,
    @Expose
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @Expose
    @SerializedName("gravatar_id")
    val gravatarId: String? = null,
    @Expose
    @SerializedName("url")
    val url: String? = null,
    @Expose
    @SerializedName("html_url")
    val htmlUrl: String? = null,
    @Expose
    @SerializedName("followers_url")
    val followersUrl: String? = null,
    @Expose
    @SerializedName("following_url")
    val followingUrl: String? = null,
    @Expose
    @SerializedName("gists_url")
    val gistsUrl: String? = null,
    @Expose
    @SerializedName("starred_url")
    val starredUrl: String? = null,
    @Expose
    @SerializedName("subscriptions_url")
    val subscriptionsUrl: String? = null,
    @Expose
    @SerializedName("organizations_url")
    val organizationsUrl: String? = null,
    @Expose
    @SerializedName("repos_url")
    val reposUrl: String? = null,
    @Expose
    @SerializedName("events_url")
    val eventsUrl: String? = null,
    @Expose
    @SerializedName("received_events_url")
    val receivedEventsUrl: String? = null,
    @Expose
    @SerializedName("type")
    val type: String? = null,
    @Expose
    @SerializedName("site_admin")
    val siteAdmin: Boolean? = null
)