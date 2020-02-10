package com.u.android_uhome.estimote

import android.app.Application
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials

class EstimoteApplication : Application() {

    val cloudCredentials = EstimoteCloudCredentials(
        "uhome-g7u",
        "edeae45dd50b1d0ff0f4efbe7f165a91"
    )

//    val cloudCredentials = EstimoteCloudCredentials(
//        R.string.estimote_app_id.toString(),
//        R.string.estimote_app_token.toString()
//    )

//    private var prefs =
//        getSharedPreferences("MyPref", Context.MODE_PRIVATE)
//    var id: String? = prefs.getString("app_id", "")
//    var token: String? = prefs.getString("app_token", "")
//
//    val cloudCredentials = EstimoteCloudCredentials(
//        id.toString(),
//        token.toString()
//    )

    fun enableBeaconNotifications(token: String) {
        val notificationsManager =
            EstimoteNotification(this)
        notificationsManager.startObserver(token)
    }

}