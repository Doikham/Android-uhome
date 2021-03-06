package com.u.android_uhome.device

import com.google.gson.annotations.SerializedName

class DeviceModel {

    data class Request(
        @SerializedName("idToken") val token: String,
        @SerializedName("RoomID") val roomId: String
    )

    data class ResponseMessage(
        @SerializedName("message") val message: List<ResponseDevicesList>
    )

    data class ResponseDevicesList(
        @SerializedName("DeviceID") val deviceId: Int,
        @SerializedName("Name") val deviceName: String,
        @SerializedName("on") val on: Boolean
    )

    data class RequestChangeColor(
        @SerializedName("idToken") val token: String,
        @SerializedName("DeviceID") val deviceId: String,
        @SerializedName("Hex") val hexValue: String,
        @SerializedName("HomeID") val homeId: String
    )

    data class Response(
        @SerializedName("message") val message: String
    )
}