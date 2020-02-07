package com.u.android_uhome.estimote

import android.app.Application
import android.util.Log
import com.estimote.internal_plugins_api.scanning.BluetoothScanner
import com.estimote.proximity_sdk.api.EstimoteCloudCredentials
import com.estimote.scanning_plugin.api.EstimoteBluetoothScannerFactory

class EstimoteApplication : Application() {

    val cloudCredentials = EstimoteCloudCredentials(
        "uhome-g7u",
        "edeae45dd50b1d0ff0f4efbe7f165a91"
    )

    fun enableBeaconNotifications(token: String) {
        val notificationsManager =
            EstimoteNotification(this)
        notificationsManager.startObserver(token)
    }

//    fun notification(token: String) {
//        val notificationsManager =
//            EstimoteNotification(this)
//        notificationsManager.startBuildNotification(token)
//    }
}