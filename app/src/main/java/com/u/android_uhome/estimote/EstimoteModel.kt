package com.u.android_uhome.estimote

import com.google.gson.annotations.SerializedName

class EstimoteModel {

    data class RequestApp(
        @SerializedName("idToken") val idToken: String,
        @SerializedName("HomeID") val homeId: String
    )

    data class ResponseAppSuccess(
        @SerializedName("AppID") val appId: String,
        @SerializedName("AppToken") val appToken: String,
        @SerializedName("message") val message: String,
        @SerializedName("EstimoteKeyExists") val estimoteKeyExist: Boolean
    )

    data class ResponseAppFailed(
        @SerializedName("message") val message: String,
        @SerializedName("EstimoteKeyExists") val estimoteKeyExist: Boolean
    )
}