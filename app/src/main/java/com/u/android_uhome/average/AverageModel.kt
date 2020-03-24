package com.u.android_uhome.average

import com.google.gson.annotations.SerializedName

class AverageModel {
    data class RequestAverageTime(
        @SerializedName("idToken") val idToken: String,
        @SerializedName("HomeID") val homeId: String,
        @SerializedName("UserID") val userId: String
    )

    data class ResponseAverage(
        @SerializedName("message") val message: Array<Array<String>>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ResponseAverage

            if (!message.contentDeepEquals(other.message)) return false

            return true
        }

        override fun hashCode(): Int {
            return message.contentDeepHashCode()
        }
    }

    data class RequestResidentList(
        @SerializedName("idToken") val idToken: String,
        @SerializedName("HomeID") val homeId: String
    )

    data class ResponseResidentList(
        @SerializedName("message") val message: List<ResponseList>
    )

    data class ResponseList(
        @SerializedName("HomeID") val homeId: String,
        @SerializedName("UserID") val userId: String,
        @SerializedName("Name") val userName: String
    )
}