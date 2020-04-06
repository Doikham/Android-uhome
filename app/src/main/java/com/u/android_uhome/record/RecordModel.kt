package com.u.android_uhome.record

import com.google.gson.annotations.SerializedName

class RecordModel {

    data class RequestRecord(
        @SerializedName("idToken") val idToken: String,
        @SerializedName("date") val date: String,
        @SerializedName("HomeID") val homeId: String
    )

    data class ResponseRecord(
        @SerializedName("message") val message: Array<Array<String>>
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ResponseRecord

            if (!message.contentEquals(other.message)) return false

            return true
        }

        override fun hashCode(): Int {
            return message.contentHashCode()
        }
    }

}