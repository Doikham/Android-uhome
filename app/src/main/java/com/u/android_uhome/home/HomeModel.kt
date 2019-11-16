package com.u.android_uhome.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HomeModel {

    data class Response(
        @SerializedName("_id") val deviceId: String,
        @SerializedName("uid") val userId: String,
        @SerializedName("name") val deviceName: String,
        @SerializedName("status") val status: String,
        @SerializedName("on") val on: Boolean
    )

    data class Request(
        @SerializedName("idToken") val token: String
    )
}