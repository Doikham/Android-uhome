package com.u.android_uhome.estimote

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class EstimoteApplication : Application() {

//    val cloudCredentials = EstimoteCloudCredentials(
//        "uhome-g7u",
//        "edeae45dd50b1d0ff0f4efbe7f165a91"
//    )

//    private val shared: SharedPreferences =
//        getSharedPreferences("MyPref", Context.MODE_PRIVATE)

//    var shared: SharedPreferences = PreferenceManager
//        .getDefaultSharedPreferences(this)
    
//    val cloudCredentials = EstimoteCloudCredentials(
//        shared.getString("app_id", ""),
//        shared.getString("app_token", "")
//    )

    fun enableBeaconNotifications(token: String , shared: SharedPreferences) {
        val notificationsManager =
            EstimoteNotification(this)
        notificationsManager.startObserver(token, shared)
    }

}