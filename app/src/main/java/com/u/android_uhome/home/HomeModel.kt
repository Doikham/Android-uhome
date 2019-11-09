package com.u.android_uhome.home

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class HomeModel {

    data class Response(
        @SerializedName("_id") @Expose val deviceId: String,
        @SerializedName("uid") @Expose val userId: Int,
        @SerializedName("name") @Expose val deviceName: String,
        @SerializedName("status") @Expose val status: String,
        @SerializedName("on") @Expose val on: Boolean
    )

    data class Request(
        @SerializedName("idToken") @Expose val token: String
    )
}