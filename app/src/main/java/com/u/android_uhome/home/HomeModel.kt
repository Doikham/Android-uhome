package com.u.android_uhome.home

import com.google.gson.annotations.SerializedName

class HomeModel {

    data class Response(
        @SerializedName("_id") val deviceId: String,
        @SerializedName("uid") val userId: Int,
        @SerializedName("name") val deviceName: String,
        @SerializedName("status") val status: String
    )
}