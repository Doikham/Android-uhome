package com.u.android_uhome.home

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

    data class RequestToggle(
        @SerializedName("idToken") val token: String,
        @SerializedName("did") val deviceId: String,
        @SerializedName("current_status") val currentStatus: String
    )

    data class ResponseToggle(
        @SerializedName("did") val deviceId: String,
        @SerializedName("current_status") val currentStatus: String
    )

    data class ResponseStartTimer(
        @SerializedName("uid") val userId: String,
        @SerializedName("time") val time: Int,
        @SerializedName("current") val current: Boolean,
        @SerializedName("_id") val id: String
    )

}