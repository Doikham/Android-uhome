package com.u.android_uhome.room

import com.google.gson.annotations.SerializedName

class RoomModel {

    data class Request(
        @SerializedName("idToken") val token: String,
        @SerializedName("homeID") val homeId: String
    )

    data class ResponseMessage(
        @SerializedName("message") val message: List<ResponseRoomList>
    )

    data class ResponseRoomList(
        @SerializedName("RoomID") val roomId: Int,
        @SerializedName("Name") val roomName: String,
        @SerializedName("Type") val roomType: String
    )

    data class RequestStartTimer(
        @SerializedName("idToken") val token: String,
        @SerializedName("RoomID") val roomId: String,
        @SerializedName("Name") val roomName: String,
        @SerializedName("Type") val roomType: String,
        @SerializedName("homeID") val homeId: String
    )

    data class ResponseStartTimer(
        @SerializedName("_id") val id: String
    )
}