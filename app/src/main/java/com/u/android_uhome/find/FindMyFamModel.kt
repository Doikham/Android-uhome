package com.u.android_uhome.find

import com.google.gson.annotations.SerializedName

class FindMyFamModel {

    data class Request(
        @SerializedName("idToken") val idToken: String,
        @SerializedName("HomeID") val homeId: String
    )

    data class Response(
        @SerializedName("message") val message: List<ResponseFamily>
    )

    data class ResponseFamily(
        @SerializedName("UserID") val userId: String,
        @SerializedName("RoomName") val roomName: String,
        @SerializedName("UserName") val userName: String,
        @SerializedName("EnterTime") val enterTime: String
    )

}