package com.u.android_uhome.estimote

import com.google.gson.annotations.SerializedName

class EstimoteModel {

    data class RequestApp(
        @SerializedName("idToken") val idToken: String,
        @SerializedName("HomeID") val homeId: String
    )

    data class ResponseApp(
        @SerializedName("AppID") val appId: String,
        @SerializedName("AppToken") val appToken: String
    )
}