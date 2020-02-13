package com.u.android_uhome.record

import com.google.gson.annotations.SerializedName

class RecordModel {

    data class RequestRecord(
        @SerializedName("idToken") val idToken: String
    )

    data class ResponseRecord(
        @SerializedName("message") val message: Array<IntArray>
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