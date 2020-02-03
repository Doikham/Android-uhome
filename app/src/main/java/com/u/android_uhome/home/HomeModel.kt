package com.u.android_uhome.home

import com.google.gson.annotations.SerializedName
import org.springframework.format.annotation.DateTimeFormat
import java.util.*
import kotlin.collections.ArrayList

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

    data class RequestSwitchLight(
        @SerializedName("idToken") val token: String,
        @SerializedName("LightId") val deviceId: String
    )

    data class ResponseSwitchLight(
        @SerializedName("message") val message: String
//        @SerializedName("current_state") val currentState: Boolean
    )

    data class ResponseToggle(
        @SerializedName("did") val deviceId: String,
        @SerializedName("current_status") val currentStatus: String
    )

    data class ResponseStartTimer(
        @SerializedName("uid") val userId: String,
        @SerializedName("time") val time: String,
        @SerializedName("current") val current: Boolean,
        @SerializedName("_id") val id: String
    )

    data class RequestStopTimer(
        @SerializedName("idToken") val token: String,
        @SerializedName("_id") val id: String
    )

    data class ResponseStopTimer(
        @SerializedName("status") val status: String
    )

    data class ResponseServerStatus(
        @SerializedName("message") val serverStatus: String
    )

    data class ResponseHomeMessage(
        @SerializedName("message") val message: List<ResponseHome>
    )

    data class ResponseHome(
        @SerializedName("HomeID") val homeId: Int,
        @SerializedName("Name") val homeName: String
    )
}